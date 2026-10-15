package com.example.shared.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a wallpaper.
 *
 * @property id Unique identifier for the wallpaper.
 * @property url The remote URL of the wallpaper image.
 * @property photographer Name of the photographer.
 * @property category The category of the wallpaper.
 */
@Serializable
data class Wallpaper(
    val id: String,
    val url: String?,
    val photographer: String?,
    val category: String?
)
