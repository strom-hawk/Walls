package com.example.trynewthingsinandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trynewthingsinandroid.imagedetail.ImageDetailsScreen
import com.example.trynewthingsinandroid.home.HomeScreen
import com.example.trynewthingsinandroid.utils.Constants

/**
 * The main navigation host for the application.
 *
 * @param modifier The modifier to be applied to the NavHost.
 * @param navController The navigation controller to manage app navigation.
 * @param startDestination The initial destination of the navigation graph.
 */
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
            arguments = listOf(navArgument(Constants.IMAGE_ID) { type = NavType.StringType })
        ) {
            ImageDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
