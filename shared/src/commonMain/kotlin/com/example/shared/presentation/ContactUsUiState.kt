package com.example.shared.presentation

import androidx.compose.runtime.Stable

/**
 * UI State for the Contact Us screen.
 *
 * @property supportEmail Support email address.
 * @property defaultSubject Default email subject line.
 * @property defaultMessage Default email body message.
 */
@Stable
data class ContactUsUiState(
    val supportEmail: String = "sauravsumanxs@gmail.com",
    val defaultSubject: String = "Samachar Feedback",
    val defaultMessage: String = ""
)
