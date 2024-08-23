package com.malism.dot.ui.widget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

abstract class UiState(
    val isLoading: Boolean = false,
    val msg: String? = null,
) {
    abstract fun empty(): Boolean
}

abstract class BaseViewModel<ST: UiState>: ViewModel() {
    var uiState by mutableStateOf<ST?>(null)
        protected set
}