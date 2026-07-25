package com.example.trynewthingsinandroid.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.trynewthingsinandroid.firebase.RemoteConfigManager
import com.example.trynewthingsinandroid.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(ImageDetailsScreenUIState())
    val uiState: StateFlow<ImageDetailsScreenUIState> = _uiState.asStateFlow()

    init {
        Timber.d("ImageDetailsViewModel init with id: $imageId")
        initialiseView()
    }

    /**
     * Handles UI events triggered from the screen.
     *
     * @param event The event to be handled.
     */
    fun onUIEvent(event: ImageDetailsScreenUIEvent) {
        when (event) {
            is ImageDetailsScreenUIEvent.OnShareClicked -> {
                Timber.d("___________ Share clicked")
            }
            is ImageDetailsScreenUIEvent.OnDownloadClicked -> {
                Timber.d("___________ Download clicked")
            }
            is ImageDetailsScreenUIEvent.OnImageClicked -> {
                _uiState.update { 
                    val nextSheet = if (it.activeBottomSheet == ImageDetailsScreenBottomSheet.Options) {
                        ImageDetailsScreenBottomSheet.None
                    } else {
                        ImageDetailsScreenBottomSheet.Options
                    }

                    println("_____________ nextSheet: $nextSheet")
                    println("_____________ it.activeBottomSheet: ${it.activeBottomSheet}")
                    it.copy(activeBottomSheet = nextSheet)
                }
            }
            is ImageDetailsScreenUIEvent.DismissBottomSheet -> {
                _uiState.update { it.copy(activeBottomSheet = ImageDetailsScreenBottomSheet.None) }
            }
        }
    }

    /**
     * Initializes the view state by fetching the selected image and setting the initial bottom sheet.
     */
    fun initialiseView() {
        imageId?.let { id ->
            val images = remoteConfigManager.getHomeWallpapers()
            val image = images.find { it.id == id }
            _uiState.update { 
                it.copy(
                    selectedImage = image,
                    activeBottomSheet = ImageDetailsScreenBottomSheet.Options
                )
            }
        }
    }
}
