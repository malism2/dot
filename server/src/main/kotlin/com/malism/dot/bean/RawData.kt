package com.malism.dot.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RawData(
    val info: Info,
    val list: Items
)

@Serializable
class Info(
    val id: String,
    val title: String,
    val description: String,
    @SerialName("display_type")
    val displayType: String,
    @SerialName("display_group")
    val displayGroup: String,
    val locale: String
)

@Serializable
class Items(val items: List<Item>)

@Serializable
class Item(val resource: Detail)

@Serializable
class Detail(
    val gizmo: Gizmo,
    val tools: List<Tool>? = null,
)

@Serializable
class Gizmo(
    val id: String,
    @SerialName("organization_id")
    val organizationId: String,
    @SerialName("short_url")
    val shortUrl: String,
    val author: Author,
    val display: Display,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)

@Serializable
class Author(
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("display_name")
    val displayName: String = "",
    @SerialName("link_to")
    val linkTo: String? = null
)

@Serializable
class Display(
    val name: String,
    val description: String,
    @SerialName("welcome_message")
    val welcomeMessage: String? = null,
    @SerialName("profile_picture_url")
    val profilePictureUrl: String = "",
    @SerialName("prompt_starters")
    val promptStarters: List<String>? = null,
)

@Serializable
class Tool(
    val id: String,
    val type: String
)

@Serializable
class Conversation(
    val id: String,
    val name: String,
    @SerialName("short_url")
    val shortUrl: String,
    @SerialName("num_conversations_str")
    val numConversationsStr: String? = null,
    @SerialName("review_total")
    val reviewTotal: Long = 0,
    @SerialName("review_count")
    val reviewCount: Long = 0,
    @SerialName("review_average")
    val reviewAverage: Float = 0f
)