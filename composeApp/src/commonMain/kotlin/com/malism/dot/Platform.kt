package com.malism.dot

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform