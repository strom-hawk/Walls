package com.example.trynewthingsinandroid.navigation

/**
 * Represents the different screens/destinations in the application.
 *
 * @property route The string route used for navigation.
 */
sealed class Screen(val route: String) {
    /**
     * The news list screen displaying Samachar feeds.
     */
    object NewsList : Screen("news_list")

    /**
     * The news detail screen displaying a specific article by index.
     */
    object NewsDetail : Screen("news_detail/{articleIndex}") {
        fun createRoute(articleIndex: Int) = "news_detail/$articleIndex"
    }
}
