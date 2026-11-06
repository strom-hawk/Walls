package com.example.trynewthingsinandroid.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.shared.domain.model.StreakInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Manages local persistence for daily app opens and streak calculations.
 */
class StreakPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Records an app open for today, updating the consecutive streak count accordingly.
     */
    fun recordAppOpen(): StreakInfo {
        val todayStr = getCurrentDateString()
        val lastActiveStr = prefs.getString(KEY_LAST_ACTIVE_DATE, "") ?: ""
        val currentStreak = prefs.getInt(KEY_STREAK_COUNT, 1)

        if (lastActiveStr != todayStr) {
            val newStreak = if (isYesterday(lastActiveStr)) {
                currentStreak + 1
            } else if (lastActiveStr.isEmpty()) {
                1
            } else {
                // Streak broken, reset to 1
                1
            }

            prefs.edit().apply {
                putString(KEY_LAST_ACTIVE_DATE, todayStr)
                putInt(KEY_STREAK_COUNT, newStreak)
                apply()
            }
        }

        return getStreakInfo()
    }

    /**
     * Retrieves current streak info without altering counters.
     */
    fun getStreakInfo(): StreakInfo {
        val streak = prefs.getInt(KEY_STREAK_COUNT, 1)
        val stars = when {
            streak >= 30 -> 5
            streak >= 14 -> 4
            streak >= 7 -> 3
            streak >= 3 -> 2
            else -> 1
        }
        val title = when (stars) {
            5 -> "Samachar Grandmaster"
            4 -> "Quantum Reader"
            3 -> "Dedicated Scholar"
            2 -> "Curious Reader"
            else -> "Novice Explorer"
        }
        return StreakInfo(streakCount = streak, starRating = stars, readerTitle = title)
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    private fun isYesterday(dateStr: String): Boolean {
        if (dateStr.isEmpty()) return false
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStr = sdf.format(cal.time)
            return dateStr == yesterdayStr
        } catch (_: Exception) {
            return false
        }
    }

    companion object {
        private const val PREF_NAME = "samachar_streak_prefs"
        private const val KEY_LAST_ACTIVE_DATE = "last_active_date"
        private const val KEY_STREAK_COUNT = "streak_count"
    }
}
