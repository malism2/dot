package com.malism.dot.task

import com.malism.dot.bean.Config
import com.malism.dot.bean.Conversation
import com.malism.dot.bean.GptGroup
import com.malism.dot.bean.GptItem
import com.malism.dot.bean.MoreInfo
import com.malism.dot.bean.RawData
import com.malism.dot.dao.ConfigDao
import com.malism.dot.dao.GptDao
import com.malism.dot.utils.json
import com.malism.dot.utils.parseTime
import io.ktor.server.application.Application
import io.ktor.server.application.log
import kotlinx.serialization.encodeToString
import org.koin.ktor.ext.get
import java.io.File
import java.io.FileFilter

object Sync {

    fun next(app: Application) =
        app.environment.config.property("ktor.sync.intern").getString().toLong()

    suspend fun run(app: Application) {
        val path = app.environment.config.property("ktor.sync.source").getString()
        val offset = app.environment.config.property("ktor.sync.offset").getString()
        val target = app.environment.config.property("ktor.sync.target").getString()

        val dir = File(path)
        if (!dir.exists() || !dir.isDirectory) {
            app.log.info("source dir not exist")
            return
        }

        val gptDao = app.get<GptDao>()
        val configDao = app.get<ConfigDao>()
        val config = configDao.get(Config.CONFIG_SYNC) ?: Config(Config.CONFIG_SYNC)
        val last = if (offset > config.value) offset else config.value

        dir.listFiles(FileFilter { it.name > last })?.forEach {
            doWork(app, gptDao, it, target)
            config.value = it.name
            configDao.set(config)
        }
    }

    private suspend fun doWork(app: Application, gptDao: GptDao, dir: File, target: String) {
        app.log.info("sync ${dir.name}")
        val rateData = readRating(app, dir)
        val d = File(dir, target)
        if (d.exists() && d.isDirectory) {
            d.listFiles(FileFilter { it.name.endsWith(".json") })?.forEach { f ->
                try {
                    app.log.info("parse ${f.path}")
                    parse(gptDao, rateData, f)
                } catch (e: Throwable) {
                    app.log.info("sync ${f.path}", e)
                }
            }
        }
    }

    private suspend fun parse(gpt: GptDao, rating: MutableMap<String, Conversation>, file: File) {
        val c = file.readText()
        val d = json().decodeFromString<RawData>(c)
        val g = GptGroup(
            gid = d.info.id,
            name = d.info.title,
            description =  d.info.description,
            type = d.info.displayType,
            group = d.info.displayGroup,
            locale = d.info.locale
        )
        var i: Conversation?
        val r = d.list.items.map {
            i = rating[it.resource.gizmo.id]
            GptItem(
                uuid = it.resource.gizmo.id,
                orgId = it.resource.gizmo.organizationId,
                name = it.resource.gizmo.display.name,
                description = it.resource.gizmo.display.description,
                avatarUrl = it.resource.gizmo.display.profilePictureUrl,
                shortUrl = it.resource.gizmo.shortUrl,
                authorId = it.resource.gizmo.author.userId,
                authorName = it.resource.gizmo.author.displayName,
                createAt = parseTime(it.resource.gizmo.createdAt),
                updateAt = parseTime(it.resource.gizmo.updatedAt),
                moreInfo = MoreInfo(
                    it.resource.gizmo.display.welcomeMessage,
                    it.resource.gizmo.display.promptStarters,
                    it.resource.tools
                ),
                isRecommend = false,
                sort = 0,
                rating = i?.reviewAverage ?: 0f,
                review = i?.reviewTotal ?: 0,
                group = g.gid
            )
        }
        if (r.isNotEmpty()) {
            gpt.sync(g, r)
        }
    }

    private suspend fun readRating(app: Application, dir: File): MutableMap<String, Conversation> {
        val rating = app.environment.config.property("ktor.sync.rating").getString()
        val file = File(dir, rating)
        app.log.info("read rating ${file.path}")
        val r = mutableMapOf<String, Conversation>()
        try {
            val t = file.readText().trimIndent()
            json().decodeFromString<List<Conversation>>(t).forEach { r[it.id] = it }
        } catch (e: Throwable) {
            app.log.info("read rating", e)
        }
        return r
    }
}