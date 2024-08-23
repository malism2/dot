package com.malism.dot

import androidx.compose.runtime.Composable
import com.malism.dot.data.API
import com.malism.dot.http.KtorClient
import com.malism.dot.ui.theme.AppTheme
import com.malism.dot.utils.Log
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
@Preview
fun App() {
    Log.init(true)
    KoinApplication(
        application = { modules(appModel()) }
    ) {
        AppTheme { AppScreen() }
    }
}

private fun appModel() = module {

}