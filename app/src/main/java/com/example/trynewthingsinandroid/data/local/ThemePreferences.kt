package com.example.trynewthingsinandroid.data.local

import android.content.Context
import android.content.SharedPreferences

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("samachar_theme_prefs", Context.MODE_PRIVATE)

    fun getSavedThemeId(defaultThemeId: String): String {
        return prefs.getString(KEY_THEME_ID, defaultThemeId) ?: defaultThemeId
    }

    fun saveThemeId(themeId: String) {
        prefs.edit().putString(KEY_THEME_ID, themeId).apply()
    }

    companion object {
        private const val KEY_THEME_ID = "selected_theme_id"
    }
}
