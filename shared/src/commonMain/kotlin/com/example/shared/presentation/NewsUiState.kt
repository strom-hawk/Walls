package com.example.shared.presentation

import androidx.compose.runtime.Stable
import com.example.shared.domain.model.Article

/**
 * Represents the UI state for the News feed screen.
 */
sealed interface NewsUiState {
    /** State representing that news feeds are currently being loaded. */
    object Loading : NewsUiState

    /**
     * State representing successful loading of news feeds.
     *
     * @property articles The list of loaded articles.
     */
    @Stable
    data class Success(val articles: List<Article>) : NewsUiState

    /**
     * State representing failure or error while loading news feeds.
     *
     * @property message Error message describing the failure.
     */
    @Stable
    data class Error(val message: String) : NewsUiState
}
