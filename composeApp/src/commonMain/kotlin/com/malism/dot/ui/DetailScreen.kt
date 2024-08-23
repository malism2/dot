package com.malism.dot.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun DetailScreen(nav: NavHostController) {
    val viewModel: DetailViewModel = viewModel()
}