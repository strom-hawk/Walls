package com.example.trynewthingsinandroid.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shared.ui.news.NewsDetailScreen
import com.example.shared.ui.news.NewsListScreen
import com.example.trynewthingsinandroid.ui.news.NewsWebViewScreen

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
            val context = LocalContext.current
            val articleIndex = backStackEntry.arguments?.getInt("articleIndex") ?: 0
            NewsDetailScreen(
                articleIndex = articleIndex,
                onBackClick = {
                    navController.popBackStack()
                },
                onReadFullNewsClick = { url ->
                    navController.navigate(Screen.NewsWebView.createRoute(url))
                },
                onShareClick = { title, url ->
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_SUBJECT, title)
                        putExtra(Intent.EXTRA_TEXT, "$title\n$url")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            )
        }
        composable(
            route = Screen.NewsWebView.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val url = java.net.URLDecoder.decode(encodedUrl, "UTF-8")
            NewsWebViewScreen(
                url = url,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
