package com.example.trynewthingsinandroid.home

import androidx.annotation.Keep

/**
 * Sealed class representing user interface events on the Home Screen.
 */
@Keep
sealed class HomeScreenUIEvent {
    /**
     * Event triggered when an image fails to load and should be removed.
     *
     * @property id The unique identifier of the image to remove.
     */
    data class RemoveImage(val id: String) : HomeScreenUIEvent()

    /**
     * Event triggered when a category is selected for filtering.
     *
     * @property category The name of the selected category.
     */
    data class SelectCategory(val category: String) : HomeScreenUIEvent()
}
