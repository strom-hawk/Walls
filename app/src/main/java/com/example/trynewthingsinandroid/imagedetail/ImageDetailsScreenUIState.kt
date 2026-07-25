package com.example.trynewthingsinandroid.imagedetail

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.example.trynewthingsinandroid.data.ImageUIState

/**
 * UI state for the Image Detail Screen.
 *
 * @property selectedImage The image to be displayed in detail.
 * @property activeBottomSheet The currently active bottom sheet.
 */
@Keep
@Stable
data class ImageDetailsScreenUIState(
    val selectedImage: ImageUIState? = null,
    val activeBottomSheet: ImageDetailsScreenBottomSheet = ImageDetailsScreenBottomSheet.None
)
