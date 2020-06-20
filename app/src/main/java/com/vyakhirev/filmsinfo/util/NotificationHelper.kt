package com.vyakhirev.filmsinfo.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vyakhirev.filmsinfo.App
import com.vyakhirev.filmsinfo.R
import com.vyakhirev.filmsinfo.view.MainActivity

class NotificationHelper(val context: Context) {

    private val prefs=App.instance!!.prefHelper

    var movieUuid = prefs.getWatchLaterUuid()
    var movieTitle = prefs.getWatchLaterTitle()
    var moviePoster = prefs.getWatchLaterPoster()
    var movieOverview = prefs.getWatchLaterOverview()

    companion object {
        const val CHANNEL_ID = "Movies channel id"
        const val NOTIFICATION_ID = 123
    }

    fun createNotification(): Notification {
        createNotificationChannel()

        Log.d("klipsa",movieTitle)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.apply {
            putExtra("uuid",movieUuid)
            putExtra("title",movieTitle)
            putExtra("poster",moviePoster)
            putExtra("overview",movieOverview)
        }

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_live_tv_yellow_24dp)
            .setContentTitle("Movie!")
            .setContentText("Need to watch this movie today: $movieTitle")
            .setStyle(
                NotificationCompat.BigTextStyle()
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        return notification
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
