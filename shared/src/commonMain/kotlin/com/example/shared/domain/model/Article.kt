package com.example.shared.domain.model

/**
 * Domain model representing a processed news article.
 *
 * @property articleId Unique identifier for the article.
 * @property title Title of the article.
 * @property description Summary or description of the article.
 * @property link Web link to the full article.
 * @property imageUrl Image URL associated with the article.
 * @property creator Author or creator of the article.
 * @property sourceName Name of the news source.
 * @property pubDate Human-readable publication date.
 * @property category Category classification of the article.
 * @property readingTime Estimated reading time.
 */
data class Article(
    val articleId: String,
    val title: String,
    val description: String,
    val link: String,
    val imageUrl: String?,
    val creator: String?,
    val sourceName: String,
    val pubDate: String?,
    val category: String?,
    val readingTime: String
)
