package com.example.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String? = null,
    val totalResults: Int? = null,
    val results: List<ArticleDto> = emptyList()
)

@Serializable
data class ArticleDto(
    @SerialName("article_id") val articleId: String,
    val title: String? = null,
    val description: String? = null,
    val link: String? = null,
    @SerialName("image_url") val imageUrl: String? = null
)
