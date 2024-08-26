package com.malism.dot.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.math.ceil

fun json() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    coerceInputValues = true
}

fun parseTime(value: String): Long {
    return Instant.parse(value).toEpochMilliseconds()
//    return LocalDateTime.parse(value.substringBefore("+")).toInstant(TimeZone.UTC).toEpochMilliseconds()
}

fun dateFormat(value: Long): String {
    return Instant.fromEpochMilliseconds(value)
        .toLocalDateTime(TimeZone.UTC).date.format(LocalDate.Formats.ISO)
}

fun ratingFormat(value: Float): String {
    return StringBuilder().apply {
        for ( i in 1..ceil(value).toInt()) {
            append("⭐")
        }
    }.toString()
}