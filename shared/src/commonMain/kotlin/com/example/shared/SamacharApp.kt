package com.example.shared

import androidx.compose.runtime.*
import com.example.shared.domain.model.Article
import com.example.shared.ui.news.NewsDetailScreen
import com.example.shared.ui.news.NewsListScreen
import com.example.shared.ui.theme.SamacharTheme

@Composable
fun SamacharApp() {
    SamacharTheme {
        var selectedArticle by remember { mutableStateOf<Article?>(null) }

        if (selectedArticle == null) {
            NewsListScreen(
                onArticleClick = { article ->
                    selectedArticle = article
                }
            )
        } else {
            NewsDetailScreen(
                article = selectedArticle!!,
                onBackClick = {
                    selectedArticle = null
                }
            )
        }
    }
}
