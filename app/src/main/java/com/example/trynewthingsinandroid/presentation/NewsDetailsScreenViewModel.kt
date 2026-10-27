package com.example.trynewthingsinandroid.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.usecase.GetNewsUseCase
import com.example.shared.presentation.NewsDetailUiState
import com.example.shared.presentation.NewsDetailUIEvent
import com.example.trynewthingsinandroid.data.local.BookmarkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder

/**
 * ViewModel managing business logic, bookmarking, and side effects for the News Detail screen.
 * Uses SavedStateHandle to retrieve articleId automatically from Navigation arguments.
 */
class NewsDetailsScreenViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val rawArticleId: String = savedStateHandle["articleId"] ?: ""
    private val articleId: String = try {
        URLDecoder.decode(rawArticleId, "UTF-8")
    } catch (_: Exception) {
        rawArticleId
    }

    private val bookmarkRepository = BookmarkRepository(application)

    private val _uiState = MutableStateFlow(NewsDetailUiState(isLoading = true))
    val uiState: StateFlow<NewsDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<NewsDetailUIEffect>()
    val uiEffect: SharedFlow<NewsDetailUIEffect> = _uiEffect.asSharedFlow()

    init {
        loadArticleAndBookmarkStatus()
    }

    private fun loadArticleAndBookmarkStatus() {
        viewModelScope.launch {
            val article = GetNewsUseCase.getCachedArticleById(articleId)
            val isBookmarked = bookmarkRepository.isBookmarked(articleId)
            _uiState.value = NewsDetailUiState(
                article = article,
                isBookmarked = isBookmarked,
                isLoading = false
            )
        }
    }

    private fun toggleBookmark() {
        viewModelScope.launch {
            val current = _uiState.value
            val article = current.article
            val newBookmarkState = !current.isBookmarked
            _uiState.value = current.copy(isBookmarked = newBookmarkState)

            if (newBookmarkState && article != null) {
                bookmarkRepository.addBookmark(article)
                _uiEffect.emit(NewsDetailUIEffect.ShowToast("News Bookmarked"))
            } else {
                bookmarkRepository.removeBookmark(articleId)
                _uiEffect.emit(NewsDetailUIEffect.ShowToast("Bookmark removed"))
            }
        }
    }

    fun onEvent(event: NewsDetailUIEvent) {
        when (event) {
            is NewsDetailUIEvent.OnBookmarkClicked -> {
                toggleBookmark()
            }
            is NewsDetailUIEvent.OnShareClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(NewsDetailUIEffect.ShareArticle(event.title, event.url))
                }
            }
            is NewsDetailUIEvent.OnReadFullNewsClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(NewsDetailUIEffect.NavigateToWebView(event.url))
                }
            }
            is NewsDetailUIEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(NewsDetailUIEffect.NavigateBack)
                }
            }
        }
    }
}
