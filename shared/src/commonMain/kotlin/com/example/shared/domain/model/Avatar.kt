package com.example.shared.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents selectable user profile avatars with associated vector icons.
 *
 * @property id Unique string identifier stored in preferences.
 * @property icon Compose [ImageVector] associated with the avatar.
 */
enum class Avatar(val id: String, val icon: ImageVector) {
    DEFAULT("default", Icons.Default.Person),
    FACE("face", Icons.Default.Face),
    ACCOUNT("account", Icons.Default.AccountCircle),
    STAR("star", Icons.Default.Star),
    SHIELD("shield", Icons.Default.Shield);

    companion object {
        /**
         * Resolves an [Avatar] from a given string identifier, defaulting to [DEFAULT] if not found.
         */
        fun fromId(id: String): Avatar {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: DEFAULT
        }
    }
}
