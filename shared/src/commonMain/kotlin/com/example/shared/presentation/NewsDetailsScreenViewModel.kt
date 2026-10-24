package com.example.shared.presentation

import com.example.shared.domain.model.Article
import com.example.shared.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsDetailsScreenViewModel(
    private val articleId: String
) {
    private val _article = MutableStateFlow<Article?>(GetNewsUseCase.getCachedArticleById(articleId))
    val article: StateFlow<Article?> = _article.asStateFlow()
}
