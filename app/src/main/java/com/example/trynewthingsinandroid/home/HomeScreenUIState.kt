package com.example.trynewthingsinandroid.home

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.example.trynewthingsinandroid.data.ImageUIState

/**
 * UI state for the Home Screen.
 *
 * @property images The list of wallpapers to display.
 */
@Keep
@Stable
data class HomeScreenUIState(
    val images: List<ImageUIState> = emptyList()
)
