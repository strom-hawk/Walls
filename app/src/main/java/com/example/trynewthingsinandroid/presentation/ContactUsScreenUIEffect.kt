package com.example.trynewthingsinandroid.presentation

/**
 * UI Side Effects for the Contact Us screen (e.g., Mail Intent, Navigation).
 */
sealed class ContactUsScreenUIEffect {
    /**
     * Effect triggered to open the external email client.
     */
    data class OpenEmailClient(val email: String, val subject: String, val message: String) : ContactUsScreenUIEffect()

    /**
     * Effect triggered to pop back stack.
     */
    data object NavigateBack : ContactUsScreenUIEffect()
}
