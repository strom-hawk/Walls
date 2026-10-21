package com.example.trynewthingsinandroid.navigation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shared.ui.news.NewsDetailScreen
import com.example.shared.ui.news.NewsListScreen
import com.example.shared.ui.profile.ProfileScreen
import com.example.trynewthingsinandroid.R
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topLevelRoutes = listOf(Screen.NewsList.route, Screen.Profile.route)
    val showBottomBar = currentRoute in topLevelRoutes

    val bottomNavItems = listOf(
        BottomNavItem(stringResource(R.string.bottom_nav_home), Screen.NewsList.route, Icons.Default.Home),
        BottomNavItem(stringResource(R.string.bottom_nav_profile), Screen.Profile.route, Icons.Default.Person)
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A0E1A))
                        .navigationBarsPadding()
                ) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFF2A3655)
                    )
                    NavigationBar(
                        containerColor = Color(0xFF0A0E1A),
                        contentColor = Color.White,
                        modifier = Modifier.height(56.dp),
                        windowInsets = WindowInsets(0, 0, 0, 0)
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (currentRoute != item.route) {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        tint = if (selected) Color(0xFF00E5FF) else Color(0xFF8A99AD),
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.label,
                                        color = if (selected) Color(0xFF00E5FF) else Color(0xFF8A99AD),
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFF00E5FF).copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.NewsList.route) {
                NewsListScreen(
                    onArticleClick = { articleId ->
                        navController.navigate(Screen.NewsDetail.createRoute(articleId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                val context = LocalContext.current
                ProfileScreen(
                    onItemClick = { itemName ->
                        val message = context.getString(R.string.under_development_fmt, itemName)
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            composable(
                route = Screen.NewsDetail.route,
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val context = LocalContext.current
                val encodedId = backStackEntry.arguments?.getString("articleId") ?: ""
                val articleId = java.net.URLDecoder.decode(encodedId, "UTF-8")
                NewsDetailScreen(
                    articleId = articleId,
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
}
