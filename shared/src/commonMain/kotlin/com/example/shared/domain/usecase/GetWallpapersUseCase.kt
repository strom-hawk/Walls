package com.example.shared.domain.usecase

import com.example.shared.domain.model.Wallpaper
import com.example.shared.domain.repository.WallpaperRepository

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
