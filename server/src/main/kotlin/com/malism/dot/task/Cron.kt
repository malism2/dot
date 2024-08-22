package com.malism.dot.task

import com.malism.dot.utils.debug
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun Application.cron() {
    Cron(this).run()
}

class Cron(private val app: Application) {

    private val scope = MainScope()

    fun run() {
        scope.launch(Dispatchers.IO) {
            Boot.run(app)
            delay(5000)
            while (isActive) {
                app.debug("run sync beginning")
                Sync.run(app)
                app.debug("run sync end")
                delay(Sync.next(app))
            }
        }
    }
}