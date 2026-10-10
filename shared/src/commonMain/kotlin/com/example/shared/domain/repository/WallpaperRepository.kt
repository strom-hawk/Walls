package com.example.shared.domain.repository

import com.example.shared.domain.model.Wallpaper

/**
 * Interface defining data operations for wallpapers.
 */
interface WallpaperRepository {
    /**
     * Retrieves all wallpapers from the data source.
     *
     * @return A Result containing the list of [Wallpaper] models.
     */
    suspend fun getAllWallpapers(): Result<List<Wallpaper>>

    /**
     * Retrieves a single wallpaper by its unique identifier.
     *
     * @param id The unique identifier of the wallpaper.
     * @return A Result containing the [Wallpaper] model if found, or null.
     */
    suspend fun getWallpaperById(id: String): Result<Wallpaper?>
}
