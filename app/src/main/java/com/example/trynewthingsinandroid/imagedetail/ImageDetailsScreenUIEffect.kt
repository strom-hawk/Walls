package com.example.trynewthingsinandroid.imagedetail

import androidx.annotation.Keep

/**
 * Sealed class representing one-time side effects for the Image Detail Screen.
 */
@Keep
sealed class ImageDetailsScreenUIEffect {
    /**
     * Effect to share an image URL.
     *
     * @property url The image URL to be shared.
     */
    data class ShareImageUrl(val url: String) : ImageDetailsScreenUIEffect()

    /**
     * Effect to download an image.
     *
     * @property url The image URL to be downloaded.
     * @property fileName The name of the file to save.
     */
    data class DownloadImage(val url: String, val fileName: String) : ImageDetailsScreenUIEffect()

    /**
     * Effect to set an image as the device wallpaper.
     *
     * @property url The image URL to be set as wallpaper.
     */
    data class SetAsWallpaper(val url: String) : ImageDetailsScreenUIEffect()

    /**
     * Effect to set an image as the device lock screen.
     *
     * @property url The image URL to be set as lock screen.
     */
    data class SetAsLockScreen(val url: String) : ImageDetailsScreenUIEffect()
}
