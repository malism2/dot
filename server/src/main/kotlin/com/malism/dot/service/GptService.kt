package com.malism.dot.service

import com.malism.dot.bean.GptItem
import com.malism.dot.dao.GptDao
import com.malism.dot.request.GptRequest
import io.ktor.http.Parameters
import org.koin.java.KoinJavaComponent.inject

class GptService {
    private val gptDao: GptDao by inject(GptDao::class.java)

    suspend fun getAll(parameters: Parameters): List<GptItem>? {
        val p = parameters["s"]
        return gptDao.getAll(p)
    }

    suspend fun getHot(parameters: Parameters): List<GptItem>? {
        val p = parameters["s"]
        return gptDao.getHot()
    }

    suspend fun getRecommend(parameters: Parameters): List<GptItem>? {
        val p = parameters["s"]
        return gptDao.getRecommend()
    }

    suspend fun getDetail(parameters: Parameters): GptItem? {
        val request = GptRequest(parameters["id"]?: "")
        return gptDao.get(request.what)
    }
}