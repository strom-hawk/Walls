package com.example.trynewthingsinandroid.home

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.example.trynewthingsinandroid.data.ImageUIState

/**
 * UI state for the Home Screen.
 *
 * @property images The list of wallpapers to display.
 * @property categories The list of unique categories available.
 * @property selectedCategory The currently selected category for filtering.
 */
@Keep
@Stable
data class HomeScreenUIState(
    val images: List<ImageUIState> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All"
)
