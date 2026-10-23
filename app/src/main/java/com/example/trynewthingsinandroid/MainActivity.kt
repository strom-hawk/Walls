package com.example.trynewthingsinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shared.ui.theme.SamacharTheme
import com.example.shared.ui.theme.ThemeManager
import com.example.trynewthingsinandroid.data.local.ThemePreferences
import com.example.trynewthingsinandroid.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val themePreferences = ThemePreferences(applicationContext)
        val savedThemeId = themePreferences.getSavedThemeId(ThemeManager.themes.first().id)
        ThemeManager.setTheme(savedThemeId)
        ThemeManager.onThemeChanged = { themeId ->
            themePreferences.saveThemeId(themeId)
        }

        enableEdgeToEdge()
        setContent {
            val currentTheme = ThemeManager.currentTheme
            val themeColorInt = currentTheme.backgroundColor.toArgb()

            LaunchedEffect(currentTheme) {
                enableEdgeToEdge(
                    statusBarStyle = if (currentTheme.isLight) SystemBarStyle.light(themeColorInt, themeColorInt) else SystemBarStyle.dark(themeColorInt),
                    navigationBarStyle = if (currentTheme.isLight) SystemBarStyle.light(themeColorInt, themeColorInt) else SystemBarStyle.dark(themeColorInt)
                )
            }

            SamacharTheme {
                AppNavHost()
            }
        }
    }
}
