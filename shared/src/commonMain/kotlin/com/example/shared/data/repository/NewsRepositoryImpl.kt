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
            val title = dto.title ?: "Untitled"
            val description = dto.description ?: ""
            val rawCreator = dto.creator?.firstOrNull()?.takeIf { it.isNotBlank() }
            val sourceName = dto.sourceName?.takeIf { it.isNotBlank() } ?: "Samachar News"
            val rawDate = dto.pubDate?.takeIf { it.isNotBlank() }
            val category = dto.category?.firstOrNull()?.uppercase()?.takeIf { it.isNotBlank() }

            val formattedDate = rawDate?.let { formatPubDate(it) }
            val readingTime = calculateReadingTime(description, title)

            Article(
                articleId = dto.articleId,
                title = title,
                description = description,
                link = dto.link ?: "",
                imageUrl = dto.imageUrl,
                creator = rawCreator,
                sourceName = sourceName,
                pubDate = formattedDate,
                category = category,
                readingTime = readingTime
            )
        }
    }

    private fun calculateReadingTime(description: String, title: String): String {
        val fullText = "$title $description"
        val wordCount = fullText.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
        val minutes = maxOf(1, wordCount / 200)
        return "$minutes min read"
    }

    private fun formatPubDate(pubDate: String): String {
        return try {
            if (pubDate.length >= 10) {
                val parts = pubDate.substring(0, 10).split("-")
                if (parts.size == 3) {
                    val year = parts[0]
                    val monthNum = parts[1].toIntOrNull() ?: 1
                    val day = parts[2]
                    val months = listOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                    val monthStr = months.getOrElse(monthNum) { "Oct" }
                    "$monthStr $day, $year"
                } else pubDate
            } else pubDate
        } catch (_: Exception) {
            pubDate
        }
    }
}
