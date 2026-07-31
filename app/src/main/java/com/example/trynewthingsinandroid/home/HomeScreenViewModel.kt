package com.example.trynewthingsinandroid.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trynewthingsinandroid.data.mapper.toUIStateList
import com.example.trynewthingsinandroid.data.repository.WallpaperRepositoryImpl
import com.example.trynewthingsinandroid.domain.usecase.GetWallpapersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home Screen, responsible for loading wallpapers from Firestore.
 */
class HomeScreenViewModel : ViewModel() {

    // Manual Dependency Injection
    private val repository = WallpaperRepositoryImpl()
    private val getWallpapersUseCase = GetWallpapersUseCase(repository)

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    
    /**
     * The observable UI state for the Home Screen.
     */
    val uiState: StateFlow<HomeScreenUIState> = _uiState.asStateFlow()

    init {
        fetchWallpapers()
    }

    /**
     * Fetches wallpapers from Firestore using the domain use case.
     */
    fun fetchWallpapers() {
        viewModelScope.launch {
            getWallpapersUseCase().onSuccess { wallpapers ->
                _uiState.update { it.copy(images = wallpapers.toUIStateList()) }
            }.onFailure {
                // Error handling can be added here (e.g., showing a snackbar)
            }
        }
    }

    /**
     * Handles UI events triggered from the screen.
     *
     * @param event The event to be handled.
     */
    fun onUIEvent(event: HomeScreenUIEvent) {
        when (event) {
            is HomeScreenUIEvent.RemoveImage -> {
                removeImage(event.id)
            }
        }
    }

    /**
     * Removes an image from the list by its ID.
     *
     * @param imageId The unique identifier of the image to remove.
     */
    private fun removeImage(imageId: String) {
        _uiState.update { currentState ->
            val updatedList = currentState.images.filter { it.id != imageId }
            currentState.copy(images = updatedList)
        }
    }
}
