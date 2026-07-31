package com.example.trynewthingsinandroid.domain.usecase

import com.example.trynewthingsinandroid.domain.model.Wallpaper
import com.example.trynewthingsinandroid.domain.repository.WallpaperRepository

/**
 * Use case to retrieve the list of all wallpapers.
 *
 * @property repository The repository to fetch data from.
 */
class GetWallpapersUseCase(private val repository: WallpaperRepository) {
    /**
     * Executes the use case.
     *
     * @return Result containing the list of [Wallpaper] objects.
     */
    suspend operator fun invoke(): Result<List<Wallpaper>> {
        return repository.getAllWallpapers()
    }
}
