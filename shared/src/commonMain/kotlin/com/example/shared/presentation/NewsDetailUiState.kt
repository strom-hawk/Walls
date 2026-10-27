package com.example.shared.presentation

import androidx.compose.runtime.Stable
import com.example.shared.domain.model.Article

/**
 * UI State for the News Detail screen.
 *
 * @property article The article details to display.
 * @property isBookmarked Whether the article is bookmarked.
 * @property isLoading Whether article details are currently loading.
 */
@Stable
data class NewsDetailUiState(
    val article: Article? = null,
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = false
)
