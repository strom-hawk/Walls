package com.example.trynewthingsinandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trynewthingsinandroid.imagedetail.ImageDetailScreen
import com.example.trynewthingsinandroid.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{imageId}") {
        fun createRoute(imageId: String) = "detail/$imageId"
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onImageClick = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("imageId") { type = NavType.StringType })
        ) {
            ImageDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
