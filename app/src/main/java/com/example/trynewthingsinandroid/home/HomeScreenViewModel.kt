package com.example.trynewthingsinandroid.home

import androidx.lifecycle.ViewModel
import com.example.trynewthingsinandroid.firebase.RemoteConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Home Screen, responsible for loading wallpapers from Remote Config.
 */
class HomeScreenViewModel : ViewModel() {

    private val remoteConfigManager = RemoteConfigManager()

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    
    /**
     * The observable UI state for the Home Screen.
     */
    val uiState: StateFlow<HomeScreenUIState> = _uiState.asStateFlow()

    init {
        // Initial load from defaults/cache
        updateImages()
        
        // Fetch and activate
        fetchWallpapers()
    }

    private fun fetchWallpapers() {
        remoteConfigManager.fetchAndActivate { success ->
            if (success) {
                updateImages()
            }
        }
    }

    private fun updateImages() {
        val images = remoteConfigManager.getHomeWallpapers()
        _uiState.update { it.copy(images = images) }
    }
}
