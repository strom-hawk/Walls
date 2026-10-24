package com.example.shared.ui.profile

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a menu item option in the user profile screen.
 *
 * @property title Display title of the profile option.
 * @property icon Vector icon associated with the option.
 */
data class ProfileOptionItem(
    val title: String,
    val icon: ImageVector
)
