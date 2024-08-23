package com.malism.dot.utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object Log {
    fun i(msg: String) = Napier.i(msg)
    fun i(msg: String, t: Throwable) = Napier.i(msg, t)
    fun d(msg: String) = Napier.d(msg)
    fun d(msg: String, t: Throwable) = Napier.d(msg, t)
    fun e(msg: String) = Napier.e(msg)
    fun e(msg: String, t: Throwable) = Napier.e(msg, t)

    fun init(isDebug: Boolean) {
        if (isDebug) {
            Napier.base(DebugAntilog())
        }
    }
}