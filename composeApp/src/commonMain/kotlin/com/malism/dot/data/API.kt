package com.malism.dot.data

import com.malism.dot.data.res.Detail
import com.malism.dot.data.res.Gpts
import com.malism.dot.http.KtorClient
import com.malism.dot.http.get
import com.malism.dot.data.res.Result

class API(private val client: KtorClient) {

    suspend fun get(): Result<List<Gpts>?> {
        return client.get(API_GET)
    }

    suspend fun getHot(): Result<List<Gpts>?> {
        return client.get(API_HOT)
    }

    suspend fun getRecommend(): Result<List<Gpts>?> {
        return client.get(API_RECOMMEND)
    }

    suspend fun getDetail(id: String): Result<Detail?> {
        return client.get(API_DETAIL, mapOf(Pair("id", id)))
    }

    companion object {
        private const val HOST = "http://192.168.1.248:8088"
        private const val TIMEOUT = 30000L
        private const val API_GET = "/g"
        private const val API_HOT = "/hot"
        private const val API_RECOMMEND = "/recommend"
        private const val API_DETAIL = "/detail"

        private val a = API(KtorClient(HOST, TIMEOUT))
        fun get() = a
    }
}