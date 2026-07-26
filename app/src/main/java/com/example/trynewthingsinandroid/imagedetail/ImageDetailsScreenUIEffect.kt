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
}
