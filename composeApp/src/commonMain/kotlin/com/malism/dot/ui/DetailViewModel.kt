package com.malism.dot.ui

import androidx.lifecycle.viewModelScope
import com.malism.dot.data.API
import com.malism.dot.data.res.Detail
import com.malism.dot.ui.widget.BaseViewModel
import com.malism.dot.ui.widget.UiState
import com.malism.dot.utils.Log
import com.malism.dot.utils.json
import dot.composeapp.generated.resources.Res
import dot.composeapp.generated.resources.search_hint
import dot.composeapp.generated.resources.tools_browser
import dot.composeapp.generated.resources.tools_code
import dot.composeapp.generated.resources.tools_dalle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.StringResource


class DetailUiState(
    isLoading: Boolean,
    msg: String? = null,
    val detail: Detail? = null,
    val flag: Int = 0
): UiState() {
    override fun empty() = detail == null
}

class DetailViewModel(private val id: String): BaseViewModel<DetailUiState>() {

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            uiState = DetailUiState(true, null, uiState?.detail, uiState?.flag ?: 0)
            val r = withContext(Dispatchers.Default) {
                try {
                    API.get().getDetail(id)
                } catch (e: Throwable) {
                    Log.d("get", e)
                    null
                }
            }
            var f = 0
            r?.data?.moreInfo?.tools?.forEach {
                when(it.type) {
                    "browser" -> f = f or 1
                    "dalle" -> f = f or 2
                    "python" -> f = f or 4
                }
            }
            uiState = DetailUiState(false, null, r?.data, f)
        }
    }
}