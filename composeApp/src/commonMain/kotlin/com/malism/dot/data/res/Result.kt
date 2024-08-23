package com.malism.dot.data.res

import kotlinx.serialization.Serializable

@Serializable
class Result<T>(val code: String, val msg: String, val data: T?) {
}