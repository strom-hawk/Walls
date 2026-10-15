package com.example.trynewthingsinandroid.data.mapper

import com.example.trynewthingsinandroid.data.ImageUIState
import com.example.trynewthingsinandroid.data.PhotoCredit
import com.example.shared.domain.model.Wallpaper

/**
 * Maps a domain [Wallpaper] object to a UI [ImageUIState] object.
 */
fun Wallpaper.toUIState(): ImageUIState {
    return ImageUIState(
        id = this.id,
        url = this.url,
        photoCredit = PhotoCredit(name = this.photographer),
        category = this.category
    )
}

/**
 * Maps a list of domain [Wallpaper] objects to a list of UI [ImageUIState] objects.
 */
fun List<Wallpaper>.toUIStateList(): List<ImageUIState> {
    return this.map { it.toUIState() }
}
