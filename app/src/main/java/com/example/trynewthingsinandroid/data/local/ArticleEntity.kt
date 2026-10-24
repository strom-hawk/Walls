package com.example.trynewthingsinandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shared.domain.model.Article

@Entity(tableName = "bookmarked_articles")
data class ArticleEntity(
    @PrimaryKey val articleId: String,
    val title: String,
    val description: String,
    val link: String,
    val imageUrl: String?,
    val creator: String?,
    val sourceName: String,
    val pubDate: String?,
    val category: String?,
    val readingTime: String
) {
    fun toDomainModel(): Article {
        return Article(
            articleId = articleId,
            title = title,
            description = description,
            link = link,
            imageUrl = imageUrl,
            creator = creator,
            sourceName = sourceName,
            pubDate = pubDate,
            category = category,
            readingTime = readingTime
        )
    }

    companion object {
        fun fromDomainModel(article: Article): ArticleEntity {
            return ArticleEntity(
                articleId = article.articleId,
                title = article.title,
                description = article.description,
                link = article.link,
                imageUrl = article.imageUrl,
                creator = article.creator,
                sourceName = article.sourceName,
                pubDate = article.pubDate,
                category = article.category,
                readingTime = article.readingTime
            )
        }
    }
}
