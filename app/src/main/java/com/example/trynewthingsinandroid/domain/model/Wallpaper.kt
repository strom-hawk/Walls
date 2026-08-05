package com.example.trynewthingsinandroid.domain.model

/**
 * Domain model representing a wallpaper.
 *
 * @property id Unique identifier for the wallpaper.
 * @property url The remote URL of the wallpaper image.
 * @property photographer Name of the photographer.
 * @property category The category of the wallpaper.
 */
data class Wallpaper(
    val id: String,
    val url: String?,
    val photographer: String?,
    val category: String?
)
