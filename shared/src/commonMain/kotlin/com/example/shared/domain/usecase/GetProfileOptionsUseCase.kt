package com.example.shared.domain.usecase

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import com.example.shared.ui.profile.ProfileOptionItem

/**
 * Use case responsible for providing profile menu options.
 * Decouples profile item configuration from the UI layer, enabling seamless integration
 * with Remote Config or dynamic backend configurations in the future.
 */
class GetProfileOptionsUseCase {
    operator fun invoke(): List<ProfileOptionItem> {
        return listOf(
            ProfileOptionItem("Shared news", Icons.Default.Share),
            ProfileOptionItem("Bookmarked news", Icons.Default.Star),
            ProfileOptionItem("Language", Icons.Default.Settings),
            ProfileOptionItem("Theme", Icons.Default.Edit),
            ProfileOptionItem("Contact us", Icons.Default.Email)
        )
    }
}
