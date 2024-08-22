package com.malism.dot.response

import com.malism.dot.bean.GptItem
import kotlinx.serialization.Serializable

@Serializable
class GptResponse(val items: List<GptItem>) {
}