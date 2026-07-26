package com.example.trynewthingsinandroid.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trynewthingsinandroid.firebase.RemoteConfigManager
import com.example.trynewthingsinandroid.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _uiEffect = MutableSharedFlow<ImageDetailsScreenUIEffect>()
    val uiEffect: SharedFlow<ImageDetailsScreenUIEffect> = _uiEffect.asSharedFlow()

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
                _uiState.value.selectedImage?.url?.let { url ->
                    viewModelScope.launch {
                        _uiEffect.emit(ImageDetailsScreenUIEffect.ShareImageUrl(url))
                    }
                }
            }
            is ImageDetailsScreenUIEvent.OnDownloadClicked -> {
                Timber.d("___________ Download clicked")
            }
            is ImageDetailsScreenUIEvent.OnImageClicked -> {
                _uiState.update { 
                    val nextFullScreen = !it.isImageFullScreen
                    it.copy(
                        isImageFullScreen = nextFullScreen,
                        activeBottomSheet = if (nextFullScreen) ImageDetailsScreenBottomSheet.Options else ImageDetailsScreenBottomSheet.None
                    )
                }
            }
            is ImageDetailsScreenUIEvent.DismissBottomSheet -> {
                _uiState.update { 
                    it.copy(
                        activeBottomSheet = ImageDetailsScreenBottomSheet.None,
                        isImageFullScreen = false
                    )
                }
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
                    activeBottomSheet = ImageDetailsScreenBottomSheet.Options,
                    isImageFullScreen = true
                )
            }
        }
    }
}
