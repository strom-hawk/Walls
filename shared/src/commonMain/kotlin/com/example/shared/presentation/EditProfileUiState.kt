package com.example.shared.presentation

import androidx.compose.runtime.Stable
import com.example.shared.domain.model.Avatar

/**
 * UI State for the Edit Profile screen.
 *
 * @property name User's display name.
 * @property email User's email address.
 * @property avatar Selected user avatar.
 * @property availableAvatars List of available avatars.
 */
@Stable
data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val avatar: Avatar = Avatar.DEFAULT,
    val availableAvatars: List<Avatar> = Avatar.entries
)
