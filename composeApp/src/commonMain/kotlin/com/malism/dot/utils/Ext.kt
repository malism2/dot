package com.malism.dot.utils

import kotlinx.serialization.json.Json

fun json() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    coerceInputValues = true
}

//fun parseTime(value: String): Long {
//    return LocalDateTime.parse(value.substringBefore("+")).toInstant(TimeZone.UTC).toEpochMilliseconds()
//}