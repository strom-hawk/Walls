package com.example.trynewthingsinandroid.presentation

/**
 * UI Side Effects for the News Detail screen (Toast, Navigation, Share Intent).
 */
sealed class NewsDetailUIEffect {
    /**
     * Effect triggered to display a Toast message.
     */
    data class ShowToast(val message: String) : NewsDetailUIEffect()

    /**
     * Effect triggered to navigate to the WebView screen.
     */
    data class NavigateToWebView(val url: String) : NewsDetailUIEffect()

    /**
     * Effect triggered to open system share sheet.
     */
    data class ShareArticle(val title: String, val url: String) : NewsDetailUIEffect()

    /**
     * Effect triggered to pop back stack.
     */
    object NavigateBack : NewsDetailUIEffect()
}
