package com.example.trynewthingsinandroid.presentation

/**
 * UI Side Effects for the Edit Profile screen (e.g., Toast, Navigation).
 */
sealed class EditProfileScreenUIEffect {
    /**
     * Effect triggered when profile has been successfully updated.
     */
    object OnProfileUpdated : EditProfileScreenUIEffect()
}
