package com.malism.dot.http

import com.malism.dot.utils.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.InternalAPI

class KtorClient(private val host: String, timeout: Long) {

    private val client = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(message)
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(json = com.malism.dot.utils.json())
        }
    }

    suspend fun <T> get(url: String, params: Map<String, Any>?, typeInfo: TypeInfo): T {
        val response = client.get(host + url) {
//            header("Authorization", "token")
            params?.forEach { parameter(it.key, it.value) }
        }
        return response.body(typeInfo)
    }

    @OptIn(InternalAPI::class)
    suspend fun <T> post(url: String, params: Any?, typeInfo: TypeInfo): T {
        val response = client.post(host + url) {
            if (params != null) body = params
        }
        return response.body(typeInfo)
    }
}

suspend inline fun <reified T> KtorClient.get(url: String, params: Map<String, Any>? = null): T {
    return get(url, params, typeInfo<T>())
}

suspend inline fun <reified T> KtorClient.post(url: String, params: Any? = null): T {
    return post(url, params, typeInfo<T>())
}