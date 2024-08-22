package com.malism.dot.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GptGroup(
    @SerialName("id")
    val gid: String,
    val name: String,
    val description: String,
    val type: String,
    val group: String,
    val locale: String
)