package com.malism.dot

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.malism.dot.ui.DetailScreen
import com.malism.dot.ui.HomeScreen
import com.malism.dot.ui.widget.LocalWindowSize

object Route {
    const val HOME = "home"
    const val DETAIL = "detail/"
}

@Composable
fun AppScreen() {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Route.HOME
    ) {
        composable(Route.HOME) {
            HomeScreen(nav)
        }
        composable(
            route = Route.DETAIL + "?id={id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            DetailScreen(nav, it.arguments?.getString("id") ?: "")
        }
    }
}