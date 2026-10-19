package com.example.shared.domain.repository

import com.example.shared.domain.model.Article

interface NewsRepository {
    suspend fun getNews(): List<Article>
}
