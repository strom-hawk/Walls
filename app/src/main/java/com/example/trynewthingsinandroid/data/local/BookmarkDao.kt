package com.example.trynewthingsinandroid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarked_articles")
    fun getAllBookmarks(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM bookmarked_articles WHERE articleId = :articleId")
    suspend fun getBookmarkById(articleId: String): ArticleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(article: ArticleEntity)

    @Query("DELETE FROM bookmarked_articles WHERE articleId = :articleId")
    suspend fun deleteBookmarkById(articleId: String)
}
