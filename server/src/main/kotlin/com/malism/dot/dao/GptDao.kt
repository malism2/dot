package com.malism.dot.dao

import com.malism.dot.bean.GptGroup
import com.malism.dot.bean.GptItem
import com.malism.dot.utils.dbQuery
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

class GptDao(database: Database) {
    object Gpts: LongIdTable("gpts") {
        val uuid = varchar("uuid", 255).uniqueIndex()
        val orgId = varchar("org_id", 255)
        val name = varchar("name", 255)
        val description = text("description")
        val avatarUrl = text("avatar_url")
        val shortUrl = varchar("short_url", 255)
        val authorId = varchar("author_id", 255)
        val authorName = varchar("author_name", 255)
        val createdAt = long("created_at")
        val updatedAt = long("updated_at")
        val detail = text("detail") // json
//        val indexUpdatedAt = integer("index_updated_at").default(0)
        val isRecommend = bool("is_recommend")
        val sort = integer("sort").default(0)
        val rating = float("rating")
        val review = long("review").default(0L)
        val group = text("group")
    }
    object Groups: LongIdTable("groupts") {
        val gid = varchar("gid", 255).uniqueIndex()
        val name = varchar("name", 255)
        val description = text("description")
        val type = varchar("type", 255)
        val group = varchar("group", 255)
        val locale = varchar("locale", 255)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Gpts)
            SchemaUtils.create(Groups)
        }
    }

    suspend fun getAll(like: String?): List<GptItem> {
        return dbQuery {
            Gpts.selectAll()
                .limit(100)
                .map { map(it) }
        }
    }

    suspend fun getHot(): List<GptItem> {
        return dbQuery {
            Gpts.selectAll()
                .limit(100)
                .map { map(it) }
        }
    }

    suspend fun getRecommend(): List<GptItem> {
        return dbQuery {
            Gpts.selectAll()
                .limit(100)
                .map { map(it) }
        }
    }

    suspend fun get(uuid: String): GptItem? {
        return dbQuery {
            Gpts.selectAll()
                .where { Gpts.uuid eq uuid }
                .limit(1)
                .map { map(it, true) }
                .singleOrNull()
        }
    }

    suspend fun delete(uuid: String) {
        dbQuery {
            Gpts.deleteWhere { Gpts.uuid eq uuid }
        }
    }

    suspend fun sync(g: GptGroup?, data: List<GptItem>) {
        if (g != null) {
            dbQuery {
                Groups.upsert {
                    it[Groups.gid] = g.gid
                    it[Groups.name] = g.name
                    it[Groups.description] = g.description
                    it[Groups.type] = g.type
                    it[Groups.group] = g.group
                    it[Groups.locale] = g.locale
                }
            }
        }
        dbQuery {
            Gpts.batchUpsert(data) {
                this[Gpts.uuid] = it.uuid
                this[Gpts.orgId] = it.orgId
                this[Gpts.name] = it.name
                this[Gpts.description] = it.description
                this[Gpts.avatarUrl] = it.avatarUrl
                this[Gpts.shortUrl] = it.shortUrl
                this[Gpts.authorId] = it.authorId
                this[Gpts.authorName] = it.authorName
                this[Gpts.createdAt] = it.createAt
                this[Gpts.updatedAt] = it.updateAt
                this[Gpts.detail] = it.detail ?: ""
                this[Gpts.isRecommend] = it.isRecommend
                this[Gpts.sort] = it.sort
                this[Gpts.rating] = it.rating
                this[Gpts.review] = it.review
                this[Gpts.group] = it.group
            }
        }
    }

    private fun map(d: ResultRow, needMore: Boolean = false): GptItem = GptItem(
        uuid = d[Gpts.uuid],
        orgId = d[Gpts.orgId],
        name = d[Gpts.name],
        description = d[Gpts.description],
        avatarUrl = d[Gpts.avatarUrl],
        shortUrl = d[Gpts.shortUrl],
        authorId = d[Gpts.authorId],
        authorName = d[Gpts.authorName],
        createAt = d[Gpts.createdAt],
        updateAt = d[Gpts.updatedAt],
        detail = if (needMore) d[Gpts.detail] else null,
        isRecommend = d[Gpts.isRecommend],
        sort = d[Gpts.sort],
        rating = d[Gpts.rating],
        review = d[Gpts.review],
        group = d[Gpts.group],
    )
}