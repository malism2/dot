package com.malism.dot

import com.malism.dot.task.cron
import io.ktor.server.application.Application

//fun main() {
//    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    setup()
    route()
    cron()
}