package com.malism.dot.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malism.dot.data.API
import com.malism.dot.data.res.Detail
import com.malism.dot.ui.widget.BaseViewModel
import com.malism.dot.ui.widget.UiState
import com.malism.dot.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUiState(
    isLoading: Boolean,
    msg: String? = null,
    val detail: Detail? = null
): UiState() {
    override fun empty() = detail == null
}

class DetailViewModel(private val id: String): BaseViewModel<DetailUiState>() {

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            uiState = DetailUiState(true)
            val r = withContext(Dispatchers.Default) {
                try {
                    API.get().getDetail(id)
                } catch (e: Throwable) {
                    Log.d("get", e)
                    null
                }
            }
            uiState = DetailUiState(false, null, r?.data)
        }
    }
}