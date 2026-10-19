package com.example.shared.domain.model

data class Article(
    val articleId: String,
    val title: String,
    val description: String,
    val link: String,
    val imageUrl: String?,
    val creator: String?,
    val sourceName: String,
    val pubDate: String?,
    val content: String,
    val category: String?,
    val readingTime: String
)
