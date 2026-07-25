package com.example.trynewthingsinandroid.firebase

import com.example.trynewthingsinandroid.R
import com.example.trynewthingsinandroid.data.ImageUIState
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

class RemoteConfigManager {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val minimumTimeIntervalInSeconds: Long = 1

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(minimumTimeIntervalInSeconds)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun getHomeWallpapers(): List<ImageUIState> {
        val json = remoteConfig.getString("home_wallpapers")
        return try {
            val type = object : TypeToken<List<ImageUIState>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            Timber.e(e, "______ Error parsing home_wallpapers JSON")
            emptyList()
        }
    }
}
