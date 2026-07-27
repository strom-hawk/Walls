package com.example.trynewthingsinandroid.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.net.toUri
import com.example.trynewthingsinandroid.R
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

/**
 * Shares an image URL using the system share sheet.
 *
 * @param context The context used to start the activity and retrieve strings.
 * @param url The image URL to be shared.
 */
fun shareImage(
    url: String,
    context: Context
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message_template, url))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

/**
 * Downloads an image using the Android DownloadManager.
 *
 * @param context The context used to access the download service.
 * @param url The URL of the image to download.
 * @param fileName The name to save the file as in the Downloads folder.
 */
fun downloadImage(
    url: String,
    fileName: String,
    context: Context
) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(url.toUri()).apply {
        setTitle(fileName)
        setDescription(context.getString(R.string.downloading_wallpaper_message))
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        setAllowedOverMetered(true)
        setAllowedOverRoaming(true)
    }
    downloadManager.enqueue(request)
}
