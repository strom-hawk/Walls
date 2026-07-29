package com.example.trynewthingsinandroid.imagedetail

import androidx.annotation.Keep

/**
 * Sealed class representing user interface events on the Image Detail Screen.
 */
@Keep
sealed class ImageDetailsScreenUIEvent {
    /**
     * Event triggered when the user clicks the share button.
     */
    data object OnShareClicked : ImageDetailsScreenUIEvent()

    /**
     * Event triggered when the user clicks the download button.
     */
    data object OnDownloadClicked : ImageDetailsScreenUIEvent()

    /**
     * Event triggered when the user clicks on the image itself.
     */
    data object OnImageClicked : ImageDetailsScreenUIEvent()

    /**
     * Event triggered when the bottom sheet should be dismissed.
     */
    data object DismissBottomSheet : ImageDetailsScreenUIEvent()

    /**
     * Event triggered when the user clicks the "Set as Wallpaper" button.
     */
    data object OnSetAsWallpaperClicked : ImageDetailsScreenUIEvent()
}
