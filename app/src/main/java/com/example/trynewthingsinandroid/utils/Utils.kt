package com.example.trynewthingsinandroid.utils

import timber.log.Timber

/**
 * Logs a message to Timber with a consistent tag prefix.
 *
 * @param tag The identifier for the source of the log.
 * @param message The message to be logged.
 */
fun log(tag: String, message: String) {
    Timber.d("_logs -> $tag : $message")
}
