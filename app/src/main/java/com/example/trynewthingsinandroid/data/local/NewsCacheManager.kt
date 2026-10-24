package com.example.trynewthingsinandroid.data.local

import android.content.Context
import com.example.shared.domain.model.Article

class NewsCacheManager(context: Context) {
    private val newsDao = AppDatabase.getInstance(context).newsDao()

    suspend fun getCachedNews(): List<Article> {
        return newsDao.getAllNews().map { it.toDomainModel() }
    }

    suspend fun saveAndPruneNews(articles: List<Article>) {
        val entities = articles.map { NewsFeedEntity.fromDomainModel(it) }
        newsDao.saveAndPruneNews(entities)
    }
}
