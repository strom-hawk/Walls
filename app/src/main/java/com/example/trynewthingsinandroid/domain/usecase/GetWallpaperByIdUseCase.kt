package com.example.trynewthingsinandroid.domain.usecase

import com.example.trynewthingsinandroid.domain.model.Wallpaper
import com.example.trynewthingsinandroid.domain.repository.WallpaperRepository

/**
 * Use case to retrieve a single wallpaper by its unique identifier.
 *
 * @property repository The repository to fetch data from.
 */
class GetWallpaperByIdUseCase(private val repository: WallpaperRepository) {
    /**
     * Executes the use case.
     *
     * @param id The unique identifier of the wallpaper.
     * @return Result containing the [Wallpaper] object if found.
     */
    suspend operator fun invoke(id: String): Result<Wallpaper?> {
        return repository.getWallpaperById(id)
    }
}
