package com.malism.dot.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.malism.dot.data.res.Gpts
import com.malism.dot.ui.widget.Content
import dot.composeapp.generated.resources.Res
import dot.composeapp.generated.resources.app_name
import dot.composeapp.generated.resources.title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(nav: NavHostController) {
    val viewModel = viewModel { HomeViewModel() }
    Content(
        title = stringResource(Res.string.app_name),
        uiState = viewModel.uiState,
        pullToRefresh = viewModel::loadData,
        navigationIcon = {
//            Icon(
//                painter = painterResource(Res.drawable.),
//            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Header()
            GPTS(viewModel)
        }
    }
}


@Composable
private fun Header() {
    Text(
        text = stringResource(Res.string.title),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun GPTS(viewModel: HomeViewModel) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(4)
    ) {
        items(viewModel.data) {
            GptItem(it)
        }
    }
}

@Composable
private fun GptItem(d: Gpts) {
    Card(
        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp)
    ) {
        Text(
            text = d.name
        )
    }
}