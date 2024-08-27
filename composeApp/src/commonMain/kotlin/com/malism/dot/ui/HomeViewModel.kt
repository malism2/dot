package com.malism.dot.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.malism.dot.data.API
import com.malism.dot.data.res.Gpts
import com.malism.dot.ui.widget.BaseViewModel
import com.malism.dot.ui.widget.UiState
import com.malism.dot.utils.Log
import com.malism.dot.utils.dateFormat
import com.malism.dot.utils.ratingFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GptsItem(val data: Gpts, val time: String, val rate: String)

class HomeUiState(
    isLoading: Boolean,
    msg: String? = null,
    private val isEmpty: Boolean = false,
): UiState(isLoading, msg) {
    override fun empty() = isEmpty
}

class HomeViewModel: BaseViewModel<HomeUiState>() {
    val data = mutableStateListOf<GptsItem>()

    init {
        loadData()
    }

    fun loadData(search: String? = null) {
        viewModelScope.launch(Dispatchers.Main) {
            uiState = HomeUiState(true, null, data.isEmpty())
            Log.d("loadData")
            val result = withContext(Dispatchers.Default) {
                try {
                    API.get().get(search)
                } catch (e: Throwable) {
                    Log.d("get", e)
                    null
                }
            }
            result?.data?.map {
                GptsItem(it, dateFormat(it.updateAt), ratingFormat(it.rating))
            }?.apply {
                data.clear()
                data.addAll(this)
            }
            uiState = HomeUiState(false, null, data.isEmpty())
        }
    }
}