package com.example.trynewthingsinandroid.ui.news

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shared.ui.theme.DarkBackground
import com.example.shared.ui.theme.NeonCyan
import com.example.shared.ui.theme.SurfaceGlass
import com.example.shared.ui.theme.BorderColor

/**
 * A composable screen that displays full news articles by loading source URLs in a native Android [WebView].
 * Features a futuristic dark theme matching the app, glassmorphism top app bar, loading progress bar,
 * and refresh controls.
 *
 * @param url The web address/URL of the full news article to load.
 * @param onBackClick Callback invoked when the user clicks the back navigation button.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsWebViewScreen(
    url: String,
    onBackClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SurfaceGlass,
                            border = BorderStroke(1.dp, BorderColor)
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.widthIn(max = 220.dp)
                        ) {
                            Text(
                                text = "SAMACHAR READER",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = url,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = SurfaceGlass,
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        IconButton(onClick = { webViewInstance?.reload() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = NeonCyan
                            )
                        }
                    }
                }

                if (isLoading) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = NeonCyan,
                        trackColor = Color.Transparent
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        webViewClient = SamacharWebViewClient(
                            onPageFinishedListener = {
                                isLoading = false
                            }
                        )
                        webChromeClient = SamacharWebChromeClient(
                            onProgressChangedListener = { newProgress ->
                                progress = newProgress
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }
                        )
                        loadUrl(url)
                        webViewInstance = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
