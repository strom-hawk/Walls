package com.example.trynewthingsinandroid.utils

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.trynewthingsinandroid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

/**
 * Fetches an image from a URL and sets it as the device wallpaper.
 *
 * @param url The image URL.
 * @param context The context used for Coil and WallpaperManager.
 */
suspend fun setAsWallpaper(
    url: String,
    context: Context
) {
    withContext(Dispatchers.IO) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)
        if (result is SuccessResult) {
            val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
            if (bitmap != null) {
                try {
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    wallpaperManager.setBitmap(bitmap)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, context.getString(R.string.wallpaper_set_success), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error setting wallpaper")
                    showErrorToast(context)
                }
            } else {
                showErrorToast(context)
            }
        } else {
            showErrorToast(context)
        }
    }
}

private suspend fun showErrorToast(context: Context) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, context.getString(R.string.wallpaper_set_failure), Toast.LENGTH_SHORT).show()
    }
}
