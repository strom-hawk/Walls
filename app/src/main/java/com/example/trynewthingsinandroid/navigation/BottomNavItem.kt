package com.example.trynewthingsinandroid.navigation

import androidx.annotation.Keep
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an item in the main bottom navigation bar.
 *
 * @property label Display label for the navigation item.
 * @property route Target destination route string.
 * @property icon Vector icon for the item.
 */
@Keep
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
