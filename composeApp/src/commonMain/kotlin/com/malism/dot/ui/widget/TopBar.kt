package com.malism.dot.ui.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    nav: NavHostController? = null,
    isMain: Boolean = false,
    navigationIcon: (@Composable () -> Unit)? = null,
    action: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier.padding(horizontal = LocalWindowSize.current.padding),
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            titleContentColor = MaterialTheme.colorScheme.onPrimary,
//        ),
        navigationIcon = {
            if (navigationIcon == null) {
                IconButton(
                    onClick = { nav?.popBackStack() },
//                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            } else {
                navigationIcon()
            }
        },
        title = {
            Text(
                modifier = if (isMain) Modifier.fillMaxWidth().padding(8.dp, 0.dp) else Modifier,
                text = title,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = action
    )
}