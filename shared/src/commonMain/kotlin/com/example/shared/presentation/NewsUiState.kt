package com.example.shared.presentation

import com.example.shared.domain.model.Article

sealed interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val articles: List<Article>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}
