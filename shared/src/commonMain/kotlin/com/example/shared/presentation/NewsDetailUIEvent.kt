package com.example.shared.presentation

/**
 * User UI Events dispatched from the News Detail screen.
 */
sealed class NewsDetailUIEvent {
    /**
     * Event triggered when the user taps the bookmark icon.
     */
    object OnBookmarkClicked : NewsDetailUIEvent()

    /**
     * Event triggered when the user taps the share button.
     */
    data class OnShareClicked(val title: String, val url: String) : NewsDetailUIEvent()

    /**
     * Event triggered when the user taps "Read Full News".
     */
    data class OnReadFullNewsClicked(val url: String) : NewsDetailUIEvent()

    /**
     * Event triggered when the user taps the back navigation button.
     */
    object OnBackClicked : NewsDetailUIEvent()
}
