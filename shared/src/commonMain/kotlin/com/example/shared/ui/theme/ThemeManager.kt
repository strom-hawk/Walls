package com.example.shared.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

data class AppThemeOption(
    val id: String,
    val name: String,
    val description: String,
    val backgroundColor: Color,
    val accentColor: Color,
    val cardColor: Color,
    val isLight: Boolean = false
)

/**
 * Global state manager for application themes.
 * Holds available futuristic themes and the currently selected theme in memory.
 */
object ThemeManager {
    val themes = listOf(
        AppThemeOption(
            id = "dark_obsidian",
            name = "Dark Obsidian",
            description = "Deep futuristic dark mode with high-contrast neon cyan highlights.",
            backgroundColor = Color(0xFF0A0E1A),
            accentColor = Color(0xFF00E5FF),
            cardColor = Color(0xFF161D31),
            isLight = false
        ),
        AppThemeOption(
            id = "light_horizon",
            name = "Light Horizon",
            description = "Clean minimalist light mode with vibrant blue accents.",
            backgroundColor = Color(0xFFF8FAFC),
            accentColor = Color(0xFF1E88E5),
            cardColor = Color(0xFFFFFFFF),
            isLight = true
        ),
        AppThemeOption(
            id = "cyberpunk_neon",
            name = "Cyberpunk Neon",
            description = "Vibrant cybernetic light aesthetic powered by radiant magenta and neon highlights.",
            backgroundColor = Color(0xFFFFEEF4),
            accentColor = Color(0xFFD5006D),
            cardColor = Color(0xFFFFFFFF),
            isLight = true
        ),
        AppThemeOption(
            id = "emerald_matrix",
            name = "Emerald Matrix",
            description = "Sleek slate dark theme infused with high-tech matrix emerald green.",
            backgroundColor = Color(0xFF021008),
            accentColor = Color(0xFF00E676),
            cardColor = Color(0xFF0A2616),
            isLight = false
        ),
        AppThemeOption(
            id = "solarized_quantum",
            name = "Solarized Quantum",
            description = "Warm solarized quantum environment highlighted with rich amber and gold.",
            backgroundColor = Color(0xFFFFFBEA),
            accentColor = Color(0xFFFF8F00),
            cardColor = Color(0xFFFFFFFF),
            isLight = true
        ),
        AppThemeOption(
            id = "deep_space_violet",
            name = "Deep Space Violet",
            description = "Cosmic indigo background enhanced with neon violet plasma.",
            backgroundColor = Color(0xFF090618),
            accentColor = Color(0xFF7C4DFF),
            cardColor = Color(0xFF151030),
            isLight = false
        )
    )

    var currentTheme by mutableStateOf(themes.first())
        private set

    fun setTheme(themeId: String) {
        themes.find { it.id == themeId }?.let {
            currentTheme = it
        }
    }
}
