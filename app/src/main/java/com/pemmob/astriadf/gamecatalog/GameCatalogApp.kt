package com.pemmob.astriadf.gamecatalog

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.astriadf.gamecatalog.ui.screen.GameDetailScreen
import com.pemmob.astriadf.gamecatalog.ui.screen.HomeScreen

// Menentukan route yang digunakan untuk setiap halaman.
private object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{gameId}"

    fun detail(gameId: Int): String {
        return "detail/$gameId"
    }
}

// Navigation utama aplikasi.
@Composable
fun GameCatalogApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        // Home Screen
        composable(
            route = Routes.HOME
        ) {

            HomeScreen(
                onGameClick = { gameId ->

                    navController.navigate(
                        Routes.detail(gameId)
                    )
                }
            )
        }

        // Detail Screen
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("gameId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val gameId =
                backStackEntry.arguments
                    ?.getInt("gameId")
                    ?: return@composable

            GameDetailScreen(
                gameId = gameId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}