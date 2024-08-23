package com.malism.dot.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json
import org.koin.compose.getKoin

fun json() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    coerceInputValues = true
}

fun parseTime(value: String): Long {
    return LocalDateTime.parse(value.substringBefore("+")).toInstant(TimeZone.UTC).toEpochMilliseconds()
}