package com.example.shared.domain.usecase

import com.example.shared.domain.model.Article
import com.example.shared.domain.repository.NewsRepository

class GetNewsUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): List<Article> {
        return repository.getNews()
    }
}
