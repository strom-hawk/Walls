package com.example.trynewthingsinandroid.navigation

/**
 * Represents the different screens/destinations in the application.
 *
 * @property route The string route used for navigation.
 */
sealed class Screen(val route: String) {
    /**
     * The news list screen displaying Samachar feeds (Home).
     */
    object NewsList : Screen("news_list")

    /**
     * The user profile screen.
     */
    object Profile : Screen("profile")

    /**
     * The themes selection screen.
     */
    object Themes : Screen("themes")

    /**
     * The bookmarked news screen.
     */
    object BookmarkedNews : Screen("bookmarked_news")

    /**
     * The news detail screen displaying a specific article by ID.
     */
    object NewsDetail : Screen("news_detail/{articleId}") {
        fun createRoute(articleId: String): String {
            val encodedId = java.net.URLEncoder.encode(articleId, "UTF-8")
            return "news_detail/$encodedId"
        }
    }

    /**
     * The web view screen displaying full news articles from source links.
     */
    object NewsWebView : Screen("news_web_view/{url}") {
        fun createRoute(url: String): String {
            val encodedUrl = java.net.URLEncoder.encode(url, "UTF-8")
            return "news_web_view/$encodedUrl"
        }
    }
}
