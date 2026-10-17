package com.example.trynewthingsinandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shared.ui.news.NewsDetailScreen
import com.example.shared.ui.news.NewsListScreen
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
    startDestination: String = Screen.NewsList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.NewsList.route) {
            NewsListScreen(
                onArticleClick = { index ->
                    navController.navigate(Screen.NewsDetail.createRoute(index))
                }
            )
        }
        composable(
            route = Screen.NewsDetail.route,
            arguments = listOf(navArgument("articleIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleIndex = backStackEntry.arguments?.getInt("articleIndex") ?: 0
            NewsDetailScreen(
                articleIndex = articleIndex,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
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
