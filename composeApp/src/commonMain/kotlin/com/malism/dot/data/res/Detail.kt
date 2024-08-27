package com.malism.dot.data.res

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Detail(
    val uuid: String,
    @SerialName("org_id")
    val orgId: String,
    val name: String,
    val description: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("short_url")
    val shortUrl: String,
    @SerialName("author_id")
    val authorId: String,
    @SerialName("author_name")
    val authorName: String,
    @SerialName("create_at")
    val createAt: Long,
    @SerialName("update_at")
    val updateAt: Long,
    @SerialName("more_info")
    val moreInfo: MoreInfo? = null,
    @SerialName("is_recommend")
    val isRecommend: Boolean,
    val sort: Int,
    val rating: Float,
    val review: Long,
    val group: String,
)

@Serializable
class MoreInfo(
    val welcome: String? = null,
    val prompts: List<String>? = null,
    val tools: List<Tool>? = null
)

@Serializable
class Tool(
    val id: String,
    val type: String
)