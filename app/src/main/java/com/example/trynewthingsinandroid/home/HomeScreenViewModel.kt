package com.example.trynewthingsinandroid.home

import androidx.lifecycle.ViewModel
import com.example.trynewthingsinandroid.data.ImageUIState
import com.example.trynewthingsinandroid.firebase.RemoteConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel : ViewModel() {

    private val remoteConfigManager = RemoteConfigManager()

    private val _imageList = MutableStateFlow<List<ImageUIState>>(emptyList())
    val imageList: StateFlow<List<ImageUIState>> = _imageList

    init {
        fetchWallpapers()
    }

    private fun fetchWallpapers() {
        remoteConfigManager.fetchAndActivate { success ->
            if (success) {
                _imageList.value = remoteConfigManager.getHomeWallpapers()
            }
        }
    }
}
