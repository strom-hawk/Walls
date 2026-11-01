package com.example.trynewthingsinandroid.presentation

/**
 * UI Events dispatched from the Edit Profile screen.
 */
sealed class EditProfileScreenUIEvent {
    /**
     * Event triggered when the user clicks the Save Profile button.
     */
    data class OnSaveClicked(val name: String, val email: String, val avatarId: String) : EditProfileScreenUIEvent()
}
