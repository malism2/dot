package com.malism.dot

import com.malism.dot.dao.ConfigDao
import com.malism.dot.dao.GptDao
import com.malism.dot.dao.UserDao
import com.malism.dot.response.Error
import com.malism.dot.response.Result
import com.malism.dot.response.json
import com.malism.dot.service.GptService
import com.malism.dot.service.HelloService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

private fun appModel(app: Application) = module {
    single { app }
//    single { app.environment.config }
    single {
        HikariDataSource(HikariConfig().apply {
            jdbcUrl = app.environment.config.property("ktor.mysql.url").getString()
            username = app.environment.config.property("ktor.mysql.user").getString()
            password = app.environment.config.property("ktor.mysql.password").getString()
            driverClassName = "com.mysql.cj.jdbc.Driver"
        })
    }
    factory {
        Database.connect(get<HikariDataSource>())
    }

    // model
    factory { UserDao(get<Database>()) }
    factory { ConfigDao(get<Database>()) }
    factory { GptDao(get<Database>()) }

    // controller
    factory { HelloService() }
    factory { GptService() }
}

fun Application.route() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/hello") {
            call.json(
                this@routing.get<HelloService>().echo(call.parameters)
            )
        }
        get("/g") {
            call.json(this@routing.get<GptService>().getAll(call.parameters))
        }
        get("/hot") {
            call.json(this@routing.get<GptService>().getHot(call.parameters))
        }
        get("/recommend") {
            call.json(this@routing.get<GptService>().getRecommend(call.parameters))
        }
        get("/detail") {
            call.json(
                this@routing.get<GptService>().getDetail(call.parameters)
            )
        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}

fun Application.setup() {
    // monitor
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    // serialization
    install(ContentNegotiation) {
        json()
    }

    // koin
    install(Koin) {
        modules(appModel(this@setup))
    }

    // error
    install(StatusPages) {
        exception<Throwable> { call, cause ->
//            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
            call.respond(
                message = Error(500, "${cause.message}"),
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}