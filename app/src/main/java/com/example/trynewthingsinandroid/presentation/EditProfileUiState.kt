package com.example.trynewthingsinandroid.presentation

import androidx.compose.runtime.Stable

/**
 * UI State for the Edit Profile screen.
 *
 * @property name User's display name.
 * @property email User's email address.
 * @property avatarId Selected avatar identifier.
 */
@Stable
data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val avatarId: String = "default"
)
