package com.example.shared.domain.usecase

import com.example.shared.domain.model.Article
import com.example.shared.domain.repository.NewsRepository

class GetNewsUseCase(
    private val repository: NewsRepository
) {
    companion object {
        private var cachedArticles: List<Article> = emptyList()

        fun getCachedArticleById(articleId: String): Article? {
            return cachedArticles.find { it.articleId == articleId }
        }
    }

    suspend operator fun invoke(): List<Article> {
        val articles = repository.getNews()
        cachedArticles = articles
        return articles
    }
}
