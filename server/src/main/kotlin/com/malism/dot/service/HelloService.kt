package com.malism.dot.service

import com.malism.dot.request.HelloRequest
import com.malism.dot.response.Result
import io.ktor.http.Parameters
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.inject

class HelloService {
    private val database: Database by inject(Database::class.java)

    fun echo(parameters: Parameters): String {
        val request = HelloRequest(parameters["name"]?: "Olm")
        return "Hello world, ${request.name}"
    }
}