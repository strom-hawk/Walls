package com.example.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the news API response envelope.
 *
 * @property status Response status string.
 * @property totalResults Total number of available results.
 * @property results List of article DTOs.
 */
@Serializable
data class NewsResponse(
    val status: String? = null,
    val totalResults: Int? = null,
    val results: List<ArticleDto> = emptyList()
)

/**
 * Data transfer object (DTO) representing a raw news article from the API.
 *
 * @property articleId Unique identifier for the article.
 * @property title Title of the article.
 * @property description Summary or description of the article.
 * @property link Web link to the full article.
 * @property imageUrl Image URL associated with the article.
 * @property creator List of creators or authors.
 * @property sourceId Identifier of the news source.
 * @property sourceName Name of the news source.
 * @property pubDate Publication date string.
 * @property category List of categories associated with the article.
 */
@Serializable
data class ArticleDto(
    @SerialName("article_id") val articleId: String,
    val title: String? = null,
    val description: String? = null,
    val link: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val creator: List<String>? = null,
    @SerialName("source_id") val sourceId: String? = null,
    @SerialName("source_name") val sourceName: String? = null,
    @SerialName("pub_date") val pubDate: String? = null,
    val category: List<String>? = null
)
