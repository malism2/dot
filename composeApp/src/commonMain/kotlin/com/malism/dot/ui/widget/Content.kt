package com.malism.dot.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController

@Composable
fun Content(
    title: String,
    modifier: Modifier = Modifier,
    nav: NavHostController? = null,
    uiState: UiState? = null,
    pullToRefresh: (() -> Unit)? = null,
    isMain: Boolean = false,
    empty: @Composable () -> Unit = {},
    navigationIcon: (@Composable () -> Unit)? = null,
    action: @Composable (RowScope.() -> Unit) = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar(title, nav, isMain, navigationIcon, action) }
    ) { p ->
        Container(
            padding = p,
            uiState = uiState,
            empty = empty,
            pullToRefresh = pullToRefresh,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(
    padding: PaddingValues,
    uiState: UiState? = null,
    empty: @Composable () -> Unit = {},
    pullToRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (pullToRefresh == null) {
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState?.isLoading == true && uiState.empty()) {
                Loading()
            } else if (uiState?.empty() == true) {
                empty()
            } else {
                content()
            }
        }
    } else {
        val state = rememberPullToRefreshState()
        if (state.isRefreshing) {
            LaunchedEffect(true) {
                if (uiState?.isLoading != true) pullToRefresh()
            }
        }
        LaunchedEffect(uiState) {
            if (uiState?.isLoading == false) state.endRefresh()
            else if (!state.isRefreshing) state.startRefresh()
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(padding)
                .nestedScroll(state.nestedScrollConnection)
        ) {
            if (uiState?.isLoading == true && uiState.empty()) {
                Loading()
            } else if (uiState?.empty() == true) {
                empty()
            } else {
                content()
            }
            PullToRefreshContainer(
                state = state,
//                contentColor = MaterialTheme.colorScheme.onSurface,
//                containerColor = Color.White,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}