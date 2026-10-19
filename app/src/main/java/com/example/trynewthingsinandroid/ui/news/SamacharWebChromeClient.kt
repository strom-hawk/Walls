package com.example.trynewthingsinandroid.ui.news

import android.webkit.WebChromeClient
import android.webkit.WebView

/**
 * Custom [WebChromeClient] implementation used in [NewsWebViewScreen] to track page loading progress
 * updates from the native Android WebView.
 *
 * @property onProgressChangedListener Callback invoked with the updated progress percentage (0 to 100).
 */
class SamacharWebChromeClient(
    private val onProgressChangedListener: (Int) -> Unit
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        onProgressChangedListener(newProgress)
    }
}
