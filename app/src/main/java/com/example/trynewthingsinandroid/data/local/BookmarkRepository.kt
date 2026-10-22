package com.example.trynewthingsinandroid.data.local

import android.content.Context
import com.example.shared.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).bookmarkDao()

    val allBookmarks: Flow<List<Article>> = dao.getAllBookmarks().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun isBookmarked(articleId: String): Boolean {
        return dao.getBookmarkById(articleId) != null
    }

    suspend fun addBookmark(article: Article) {
        dao.insertBookmark(ArticleEntity.fromDomainModel(article))
    }

    suspend fun removeBookmark(articleId: String) {
        dao.deleteBookmarkById(articleId)
    }
}
