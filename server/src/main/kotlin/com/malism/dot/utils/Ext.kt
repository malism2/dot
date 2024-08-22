package com.malism.dot.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.format.DateTimeFormatter

fun json() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    coerceInputValues = true
}

fun parseTime(value: String): Long {
    return LocalDateTime.parse(value.substringBefore("+")).toInstant(TimeZone.UTC).toEpochMilliseconds()
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }