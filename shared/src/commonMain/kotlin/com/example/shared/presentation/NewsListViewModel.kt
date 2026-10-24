package com.example.shared.presentation

import com.example.shared.data.repository.NewsRepositoryImpl
import com.example.shared.domain.model.Article
import com.example.shared.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val getNewsUseCase: GetNewsUseCase = GetNewsUseCase(NewsRepositoryImpl()),
    private val loadCachedNews: suspend () -> List<Article> = { emptyList() },
    private val saveNewsToCache: suspend (List<Article>) -> Unit = {}
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val articles = getNewsUseCase()
                saveNewsToCache(articles)
                val allCached = loadCachedNews()
                val displayList = if (allCached.isNotEmpty()) allCached else articles
                GetNewsUseCase.setCachedArticles(displayList)
                _uiState.value = NewsUiState.Success(displayList)
            } catch (e: Exception) {
                try {
                    val cached = loadCachedNews()
                    if (cached.isNotEmpty()) {
                        GetNewsUseCase.setCachedArticles(cached)
                        _uiState.value = NewsUiState.Success(cached)
                    } else {
                        _uiState.value = NewsUiState.Error(e.message ?: "Unknown error")
                    }
                } catch (_: Exception) {
                    _uiState.value = NewsUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
