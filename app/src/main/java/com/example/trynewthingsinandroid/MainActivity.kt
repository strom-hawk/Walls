package com.example.trynewthingsinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.trynewthingsinandroid.navigation.AppNavHost
import com.example.trynewthingsinandroid.ui.theme.TryNewThingsInAndroidTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TryNewThingsInAndroidTheme {
                AppNavHost()
            }
        }
    }
}
