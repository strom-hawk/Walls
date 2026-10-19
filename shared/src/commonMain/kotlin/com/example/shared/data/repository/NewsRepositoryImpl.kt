package com.example.shared.data.repository

import com.example.shared.data.remote.NewsApiService
import com.example.shared.domain.model.Article
import com.example.shared.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val apiService: NewsApiService = NewsApiService()
) : NewsRepository {
    override suspend fun getNews(): List<Article> {
        val response = apiService.getNews()
        return response.results.map { dto ->
            Article(
                articleId = dto.articleId,
                title = dto.title ?: "Untitled",
                description = dto.description ?: "",
                link = dto.link ?: "",
                imageUrl = dto.imageUrl
            )
        }
    }
}
