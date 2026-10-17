package com.example.shared.util

import androidx.compose.runtime.Composable

expect class TextToSpeechPlayer {
    fun speak(text: String)
    fun stop()
    fun shutdown()
}

@Composable
expect fun rememberTextToSpeechPlayer(onDone: () -> Unit = {}): TextToSpeechPlayer
