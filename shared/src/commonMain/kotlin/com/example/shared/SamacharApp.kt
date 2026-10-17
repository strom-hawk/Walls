package com.example.shared

import androidx.compose.runtime.*
import com.example.shared.ui.news.NewsListScreen
import com.example.shared.ui.theme.SamacharTheme

@Composable
fun SamacharApp() {
    SamacharTheme {
        NewsListScreen(
            onArticleClick = { _ -> }
        )
    }
}
