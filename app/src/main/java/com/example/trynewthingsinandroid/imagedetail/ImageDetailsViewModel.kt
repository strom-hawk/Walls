package com.example.trynewthingsinandroid.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.trynewthingsinandroid.data.ImageUIState
import com.example.trynewthingsinandroid.firebase.RemoteConfigManager
import com.example.trynewthingsinandroid.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * ViewModel for the Image Detail Screen, responsible for finding the selected image by ID.
 *
 * @param savedStateHandle Handle to the state saved across process death and navigation.
 */
class ImageDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val remoteConfigManager = RemoteConfigManager()
    private val imageId: String? = savedStateHandle[Constants.IMAGE_ID]

    private val _selectedImage = MutableStateFlow<ImageUIState?>(null)
    
    /**
     * The observable selected image state.
     */
    val selectedImage: StateFlow<ImageUIState?> = _selectedImage.asStateFlow()

    init {
        Timber.d("ImageDetailsViewModel init with id: $imageId")
        imageId?.let { id ->
            val images = remoteConfigManager.getHomeWallpapers()
            _selectedImage.value = images.find { it.id == id }
        }
    }
}
