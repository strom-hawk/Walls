package com.example.trynewthingsinandroid.data.repository

import com.example.trynewthingsinandroid.domain.model.Wallpaper
import com.example.trynewthingsinandroid.domain.repository.WallpaperRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.tasks.await

/**
 * Implementation of [WallpaperRepository] using Firebase Firestore with in-memory and disk caching.
 */
class WallpaperRepositoryImpl : WallpaperRepository {

    private val collectionName = "wallpapers"

    companion object {
        private val firestore: FirebaseFirestore by lazy {
            val db = FirebaseFirestore.getInstance()
            val settings = firestoreSettings {
                // Explicitly enable persistent disk cache (enabled by default, but configured here for clarity)
                setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            }
            db.firestoreSettings = settings
            db
        }

        // In-memory cache to share data between screens
        private var cachedWallpapers: List<Wallpaper>? = null
    }

    override suspend fun getAllWallpapers(): Result<List<Wallpaper>> {
        // Return from in-memory cache if available
        cachedWallpapers?.let {
            return Result.success(it)
        }

        return try {
            val snapshot = firestore.collection(collectionName).get().await()
            val wallpapers = snapshot.documents.map { doc ->
                Wallpaper(
                    id = doc.id,
                    url = doc.getString("url"),
                    photographer = doc.getString("photographer")
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

        // 2. Fetch from Firestore (will automatically check disk cache then server)
        return try {
            val doc = firestore.collection(collectionName).document(id).get().await()
            if (doc.exists()) {
                val wallpaper = Wallpaper(
                    id = doc.id,
                    url = doc.getString("url"),
                    photographer = doc.getString("photographer")
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
