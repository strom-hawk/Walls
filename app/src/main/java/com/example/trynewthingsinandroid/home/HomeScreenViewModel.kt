package com.example.trynewthingsinandroid.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trynewthingsinandroid.data.ImageUIState
import com.example.trynewthingsinandroid.data.mapper.toUIStateList
import com.example.shared.data.repository.WallpaperRepositoryImpl
import com.example.shared.domain.usecase.GetWallpapersUseCase
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

    private var allImages: List<ImageUIState> = emptyList()

    init {
        fetchWallpapers()
    }

    /**
     * Fetches wallpapers from Firestore using the domain use case.
     */
    fun fetchWallpapers() {
        viewModelScope.launch {
            getWallpapersUseCase().onSuccess { wallpapers ->
                val uiImages = wallpapers.toUIStateList()
                allImages = uiImages
                
                val categories = uiImages.mapNotNull { it.category }.distinct().sorted()
                val fullCategoriesList = listOf("All") + categories

                _uiState.update { 
                    it.copy(
                        images = uiImages,
                        categories = fullCategoriesList
                    )
                }
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
            is HomeScreenUIEvent.SelectCategory -> {
                filterByCategory(event.category)
            }
        }
    }

    /**
     * Filters the images based on the selected category.
     *
     * @param category The category to filter by.
     */
    private fun filterByCategory(category: String) {
        _uiState.update { currentState ->
            val filteredList = if (category == "All") {
                allImages
            } else {
                allImages.filter { it.category == category }
            }
            currentState.copy(
                selectedCategory = category,
                images = filteredList
            )
        }
    }

    /**
     * Removes an image from the list by its ID.
     *
     * @param imageId The unique identifier of the image to remove.
     */
    private fun removeImage(imageId: String) {
        allImages = allImages.filter { it.id != imageId }
        _uiState.update { currentState ->
            val updatedList = currentState.images.filter { it.id != imageId }
            currentState.copy(images = updatedList)
        }
    }
}
