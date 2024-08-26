package com.malism.dot.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.malism.dot.Route
import com.malism.dot.data.res.Gpts
import com.malism.dot.ui.widget.Content
import dot.composeapp.generated.resources.Res
import dot.composeapp.generated.resources.app_name
import dot.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(nav: NavHostController) {
    val viewModel = viewModel { HomeViewModel() }
    var search by rememberSaveable { mutableStateOf("") }

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
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = StaggeredGridCells.Adaptive(minSize = 320.dp),
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Header(search, onQuery = { search = it }, onSearch = {})
            }
            items(viewModel.data) {
                GptItem(it) {
                    nav.navigate("${Route.DETAIL}?id=${it.uuid}")
                }
            }
        }
    }
}


@Composable
private fun Header(search: String, onQuery: (String) -> Unit, onSearch: () -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.title),
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp).widthIn(max = 480.dp)
                .fillMaxWidth(),
            value = search,
            onValueChange = onQuery,
        )
    }
}

@Composable
private fun GptItem(d: GptsItem, onClick: (Gpts) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp).clickable { onClick(d.data) },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(76.dp).clip(RoundedCornerShape(8.dp)),
                model = d.data.avatarUrl,
                contentDescription = "avatar"
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = d.data.name
                )
                Text(
                    text = d.data.authorName
                )
            }
        }
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = d.data.description
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(16.dp).weight(1.0f),
                text = d.rate,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Green
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = d.time
            )
        }
    }
}