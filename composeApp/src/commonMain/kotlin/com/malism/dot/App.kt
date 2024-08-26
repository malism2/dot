package com.malism.dot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import com.malism.dot.ui.theme.AppTheme
import com.malism.dot.ui.widget.LocalWindowSize
import com.malism.dot.ui.widget.WindowSize
import com.malism.dot.utils.Log
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    Log.init(true)
    KoinApplication(
        application = { modules(appModel()) }
    ) {
        AppTheme {
            CompositionLocalProvider(
                LocalWindowSize provides WindowSize.compute(LocalWindowInfo.current.containerSize.width)
            ) {
                AppScreen()
            }
        }
    }
}

private fun appModel() = module {

}