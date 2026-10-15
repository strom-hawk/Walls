package com.example.shared.data.repository

import com.example.shared.domain.model.Wallpaper
import com.example.shared.domain.repository.WallpaperRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

/**
 * Cross-platform implementation of [WallpaperRepository] using Firebase Firestore.
 */
class WallpaperRepositoryImpl : WallpaperRepository {

    private val collectionName = "wallpapers"

    companion object {
        private val firestore by lazy { Firebase.firestore }

        // In-memory cache to share data between screens/calls
        private var cachedWallpapers: List<Wallpaper>? = null
    }

    override suspend fun getAllWallpapers(): Result<List<Wallpaper>> {
        // Return from in-memory cache if available
        cachedWallpapers?.let {
            return Result.success(it)
        }

        return try {
            val snapshot = firestore.collection(collectionName).get()
            val wallpapers = snapshot.documents.map { doc ->
                Wallpaper(
                    id = doc.id,
                    url = doc.get("url"),
                    photographer = doc.get("photographer"),
                    category = doc.get("category")
                )
            }

            // Update in-memory cache
            cachedWallpapers = wallpapers

            Result.success(wallpapers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWallpaperById(id: String): Result<Wallpaper?> {
        // 1. Check in-memory cache first
        cachedWallpapers?.find { it.id == id }?.let {
            return Result.success(it)
        }

        // 2. Fetch from Firestore
        return try {
            val doc = firestore.collection(collectionName).document(id).get()
            if (doc.exists) {
                val wallpaper = Wallpaper(
                    id = doc.id,
                    url = doc.get("url"),
                    photographer = doc.get("photographer"),
                    category = doc.get("category")
                )
                Result.success(wallpaper)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
