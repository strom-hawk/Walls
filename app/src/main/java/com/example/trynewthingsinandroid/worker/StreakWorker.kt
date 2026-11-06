package com.example.trynewthingsinandroid.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.trynewthingsinandroid.MainActivity
import com.example.trynewthingsinandroid.R
import com.example.trynewthingsinandroid.data.local.StreakPreferences
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Background WorkManager worker responsible for triggering a daily 11:00 AM reading streak notification.
 */
class StreakWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val streakPrefs = StreakPreferences(context)
        val streakInfo = streakPrefs.getStreakInfo()

        triggerNotification(context, streakInfo.streakCount)
        return Result.success()
    }

    private fun triggerNotification(context: Context, streakCount: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "samachar_streak_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Reading Streak",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders to maintain your daily reading streak"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🔥 Protect Your $streakCount-Day Reading Streak!")
            .setContentText("Quantum feeds are waiting for you. Open Samachar now to keep your momentum alive!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Quantum feeds are waiting for you. Open Samachar now to keep your $streakCount-day streak alive and unlock new grandmaster achievements!")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1001, notification)
    }

    companion object {
        fun scheduleDailyNotification(context: Context) {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 11)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val initialDelay = dueDate.timeInMillis - currentDate.timeInMillis

            val request = PeriodicWorkRequestBuilder<StreakWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "DailyStreakNotification",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
