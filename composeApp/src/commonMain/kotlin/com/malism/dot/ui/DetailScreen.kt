package com.malism.dot.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.malism.dot.data.res.Detail
import com.malism.dot.ui.widget.Content
import com.malism.dot.ui.widget.LocalWindowSize
import com.malism.dot.utils.dateFormat
import dot.composeapp.generated.resources.Res
import dot.composeapp.generated.resources.capabilities
import dot.composeapp.generated.resources.created_at
import dot.composeapp.generated.resources.created_by
import dot.composeapp.generated.resources.preview
import dot.composeapp.generated.resources.prompt_starter
import dot.composeapp.generated.resources.tools_browser
import dot.composeapp.generated.resources.tools_code
import dot.composeapp.generated.resources.tools_dalle
import dot.composeapp.generated.resources.try_on_gpt
import dot.composeapp.generated.resources.what_is_this
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun DetailScreen(nav: NavHostController, id: String) {
    val viewModel = viewModel { DetailViewModel(id) }

    Content(
        title = viewModel.uiState?.detail?.name ?: "",
        uiState = viewModel.uiState,
        pullToRefresh = viewModel::loadData,
        nav = nav
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            viewModel.uiState?.detail?.let {
                if (LocalWindowSize.current.isLarge()) {
                    Spacer(Modifier.height(128.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Detail(it, viewModel.uiState?.flag ?: 0, Modifier.weight(1.0f))
                        Spacer(Modifier.width(24.dp))
                        Preview(it, modifier = Modifier.weight(1.0f))
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Detail(it, viewModel.uiState?.flag ?: 0, Modifier.fillMaxWidth())
                        Spacer(Modifier.height(24.dp))
                        Preview(it, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(d: Detail, flag: Int, modifier: Modifier) {
    val uriHandler = LocalUriHandler.current
    Column(modifier) {
        Text(
            text = stringResource(Res.string.created_at, dateFormat(d.createAt)),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.created_by, d.authorName),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            text = stringResource(Res.string.what_is_this, d.name),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = d.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp).height(1.dp)
            .background(MaterialTheme.colorScheme.onBackground)
        )
        Text(
            text = stringResource(Res.string.capabilities, d.name)
        )
        TextCheckBox(Res.string.tools_browser, (flag and 1) != 0)
        TextCheckBox(Res.string.tools_dalle, (flag and 2) != 0)
        TextCheckBox(Res.string.tools_code, (flag and 4) != 0)

        Button(
            modifier = Modifier.padding(top = 24.dp).height(44.dp).fillMaxWidth(),
            onClick = {
                // https://chatgpt.com/
                uriHandler.openUri("https://chatgpt.com/${d.shortUrl}")
            }
        ) {
            Text(text = stringResource(Res.string.try_on_gpt, d.name))
        }
    }
}

@Composable
fun Preview(d: Detail, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp).fillMaxWidth()) {
            AsyncImage(
                modifier = Modifier.size(128.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(16.dp)),
                model = d.avatarUrl,
                contentDescription = null,
//                onError = {
//                    Log.d("load image ${viewModel.uiState?.detail?.avatarUrl}", it.result.throwable)
//                }
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
                text = stringResource(Res.string.preview, d.name)
            )
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = d.moreInfo?.welcome ?: "",
                style =  MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                text = stringResource(Res.string.prompt_starter, d.name)
            )
            d.moreInfo?.prompts?.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun TextCheckBox(text: StringResource, checked: Boolean) {
    Row(
        modifier = Modifier.padding(start = 4.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked =  checked,
            onCheckedChange = null,
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}