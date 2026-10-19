package com.example.trynewthingsinandroid.ui.news

import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Custom [WebViewClient] implementation used in [NewsWebViewScreen] to monitor page loading completion
 * and notify observers when a web page finishes loading.
 *
 * @property onPageFinishedListener Callback invoked when page loading is complete.
 */
class SamacharWebViewClient(
    private val onPageFinishedListener: () -> Unit
) : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onPageFinishedListener()
    }
}
