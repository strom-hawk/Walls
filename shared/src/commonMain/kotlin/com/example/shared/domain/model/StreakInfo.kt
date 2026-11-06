package com.example.shared.domain.model

import androidx.compose.runtime.Stable

/**
 * Data class representing the user's reading streak metrics.
 *
 * @property streakCount Current consecutive days streak count.
 * @property starRating Star level (1 to 5).
 * @property readerTitle Motivational reader title corresponding to star rank.
 */
@Stable
data class StreakInfo(
    val streakCount: Int = 1,
    val starRating: Int = 1,
    val readerTitle: String = "Novice Explorer"
)
