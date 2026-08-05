package com.example.trynewthingsinandroid.data

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

/**
 * UI state representing an image in the gallery.
 *
 * @property id The unique identifier of the image.
 * @property url The remote URL of the image.
 * @property photoCredit The credit information for the photographer.
 * @property category The category of the image.
 */
@Keep
@Stable
data class ImageUIState(
    val id: String? = null,
    val url: String? = null,
    val photoCredit: PhotoCredit? = null,
    val category: String? = null
)

/**
 * Data class representing photo credit information.
 *
 * @property name The name of the photographer.
 */
@Keep
@Stable
data class PhotoCredit(
    val name: String? = null
)
