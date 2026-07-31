package com.example.trynewthingsinandroid.data.repository

import com.example.trynewthingsinandroid.domain.model.Wallpaper
import com.example.trynewthingsinandroid.domain.repository.WallpaperRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Implementation of [WallpaperRepository] using Firebase Firestore.
 */
class WallpaperRepositoryImpl : WallpaperRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionName = "wallpapers"

    override suspend fun getAllWallpapers(): Result<List<Wallpaper>> {
        return try {
            val snapshot = firestore.collection(collectionName).get().await()
            val wallpapers = snapshot.documents.map { doc ->
                Wallpaper(
                    id = doc.id,
                    url = doc.getString("url"),
                    photographer = doc.getString("photographer")
                )
            }

            Result.success(wallpapers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWallpaperById(id: String): Result<Wallpaper?> {
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
