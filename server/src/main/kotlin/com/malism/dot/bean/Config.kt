package com.malism.dot.bean

import kotlinx.serialization.Serializable

@Serializable
class Config(
    val name: String,
    var value: String = "",
    var value1: String = "",
    var value2: String = ""
) {
    companion object {
        const val CONFIG_SYNC = "remote_sync"
    }
}