package com.example.trynewthingsinandroid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_feed_articles ORDER BY timestamp DESC")
    suspend fun getAllNews(): List<NewsFeedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(newsList: List<NewsFeedEntity>)

    @Query("DELETE FROM news_feed_articles WHERE articleId NOT IN (SELECT articleId FROM news_feed_articles ORDER BY timestamp DESC LIMIT 50)")
    suspend fun pruneOldNews()

    @Transaction
    suspend fun saveAndPruneNews(newsList: List<NewsFeedEntity>) {
        insertNewsList(newsList)
        pruneOldNews()
    }
}
