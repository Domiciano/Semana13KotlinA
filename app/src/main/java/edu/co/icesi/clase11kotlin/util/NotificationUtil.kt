package edu.co.icesi.clase11kotlin.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import edu.co.icesi.clase11kotlin.R

class NotificationUtil {

    private val CHANNEL_ID = "messages"
    private val CHANNEL_NAME = "Messages"
    private val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

    fun createNotification(context: Context, title: String?, msg: String?, intent: Intent?) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE)
            manager.createNotificationChannel(channel)
        }
        val pendingIntent =
            PendingIntent.getActivity(context, idCounter, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.notificationicon)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notification = builder.build()
        manager.notify(idCounter, notification)
        idCounter++
    }

    companion object{
        @JvmStatic
        private var idCounter = 0
    }
}