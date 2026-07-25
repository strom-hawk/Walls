package com.example.trynewthingsinandroid.data

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

/**
 * UI state representing an image in the gallery.
 *
 * @property id The unique identifier of the image.
 * @property url The remote URL of the image.
 */
@Keep
@Stable
data class ImageUIState(
    val id: String,
    val url: String
)
