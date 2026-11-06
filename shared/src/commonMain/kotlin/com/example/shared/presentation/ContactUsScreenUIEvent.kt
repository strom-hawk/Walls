package com.example.shared.presentation

/**
 * User UI Events dispatched from the Contact Us screen.
 */
sealed class ContactUsScreenUIEvent {
    /**
     * Event triggered when the user clicks the email address to compose feedback.
     */
    data class OnEmailClicked(val email: String, val subject: String, val message: String) : ContactUsScreenUIEvent()

    /**
     * Event triggered when the user clicks the back navigation button.
     */
    data object OnBackClicked : ContactUsScreenUIEvent()
}
