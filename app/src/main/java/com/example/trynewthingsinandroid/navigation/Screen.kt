package com.example.trynewthingsinandroid.navigation

import com.example.trynewthingsinandroid.utils.Constants

/**
 * Represents the different screens/destinations in the application.
 *
 * @property route The string route used for navigation.
 */
sealed class Screen(val route: String) {
    /**
     * The home screen displaying the wallpaper gallery.
     */
    object Home : Screen("home")

    /**
     * The detail screen displaying a specific image.
     */
    object Detail : Screen("detail/{${Constants.IMAGE_ID}}") {
        /**
         * Creates a navigation route for a specific image ID.
         *
         * @param imageId The unique identifier of the image.
         */
        fun createRoute(imageId: String) = "detail/$imageId"
    }
}
