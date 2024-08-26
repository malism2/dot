package com.malism.dot.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.malism.dot.ui.widget.Content
import com.malism.dot.utils.Log

@Composable
fun DetailScreen(nav: NavHostController, id: String) {
    val viewModel = viewModel { DetailViewModel(id) }

    Content(
        title = viewModel.uiState?.detail?.name ?: "",
        uiState = viewModel.uiState,
        nav = nav
    ) {
        Column {
            AsyncImage(
                modifier = Modifier.size(128.dp),
                model = viewModel.uiState?.detail?.avatarUrl,
                contentDescription = null,
                onError = {
                    Log.d("load image ${viewModel.uiState?.detail?.avatarUrl}", it.result.throwable)
                }
            )
        }
    }
}