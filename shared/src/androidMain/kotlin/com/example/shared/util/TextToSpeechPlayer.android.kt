package com.example.shared.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual class TextToSpeechPlayer(
    context: Context,
    private val onDone: () -> Unit
) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                isInitialized = true
                @Suppress("DEPRECATION")
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        onDone()
                    }
                    override fun onError(utteranceId: String?) {
                        onDone()
                    }
                })
            }
        }
    }

    actual fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NewsUtteranceId")
        }
    }

    actual fun stop() {
        tts?.stop()
    }

    actual fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

@Composable
actual fun rememberTextToSpeechPlayer(onDone: () -> Unit): TextToSpeechPlayer {
    val context = LocalContext.current
    val player = remember { TextToSpeechPlayer(context, onDone) }
    
    DisposableEffect(player) {
        onDispose {
            player.shutdown()
        }
    }
    return player
}
