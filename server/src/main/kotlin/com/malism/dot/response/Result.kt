package com.malism.dot.response

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
class Result<T>(val code: Int, val msg: String, val data: T)

@Serializable
class Empty(val code: Int = 0, val msg: String = "")

@Serializable
class Error(val code: Int, val msg: String)

suspend inline fun <reified T : Any> ApplicationCall.json(data: T?) {
    if (data == null) respond(Empty())
    else respond(Result(0, "", data))
}