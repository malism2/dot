package com.malism.dot.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.malism.dot.data.API
import com.malism.dot.data.res.Gpts
import com.malism.dot.ui.widget.BaseViewModel
import com.malism.dot.ui.widget.UiState
import com.malism.dot.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeUiState(
    isLoading: Boolean,
    msg: String? = null,
    private val isEmpty: Boolean = false,
): UiState(isLoading, msg) {
    override fun empty() = isEmpty
}

class HomeViewModel: BaseViewModel<HomeUiState>() {
    val data = mutableStateListOf<Gpts>()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.Main) {
            uiState = HomeUiState(true, null, data.isEmpty())
            Log.d("loadData")
            val result = withContext(Dispatchers.Default) {
                try {
                    API.get().get()
                } catch (e: Throwable) {
                    Log.d("get", e)
                    null
                }
            }
            result?.data?.apply {
                data.clear()
                data.addAll(this)
            }
            uiState = HomeUiState(false, null, data.isEmpty())
        }
    }
}