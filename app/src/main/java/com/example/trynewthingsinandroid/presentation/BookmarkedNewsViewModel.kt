package com.example.trynewthingsinandroid.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.model.Article
import com.example.trynewthingsinandroid.data.local.BookmarkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarkedNewsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookmarkRepository(application)

    val bookmarkedArticles: StateFlow<List<Article>> = repository.allBookmarks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeBookmark(articleId: String) {
        viewModelScope.launch {
            repository.removeBookmark(articleId)
        }
    }
}
