package com.example.trynewthingsinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shared.ui.theme.SamacharTheme
import com.example.trynewthingsinandroid.navigation.AppNavHost
import com.example.trynewthingsinandroid.ui.theme.AppDarkBackgroundColorInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AppDarkBackgroundColorInt),
            navigationBarStyle = SystemBarStyle.dark(AppDarkBackgroundColorInt)
        )
        setContent {
            SamacharTheme {
                AppNavHost()
            }
        }
    }
}
