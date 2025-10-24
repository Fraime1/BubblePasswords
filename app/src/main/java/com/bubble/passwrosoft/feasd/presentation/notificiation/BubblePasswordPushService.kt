package com.bubble.passwrosoft.feasd.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.bubble.passwrosoft.BubblePasswordActivity
import com.bubble.passwrosoft.R
import com.bubble.passwrosoft.feasd.presentation.app.BubblePasswordApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private val BUBBLE_PASSWORD_CHANNEL_ID = "bubble_password_notifications"

class BubblePasswordPushService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                bubblePasswordShowNotification(it.title ?: "BubblePassword", it.body ?: "", data = remoteMessage.data["url"])
            } else {
                bubblePasswordShowNotification(it.title ?: "BubblePassword", it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            bubblePasswordHandleDataPayload(remoteMessage.data)
        }
    }

    private fun bubblePasswordShowNotification(title: String, message: String, data: String?) {
        val bubblePasswordNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                BUBBLE_PASSWORD_CHANNEL_ID,
                "EggSafe Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            bubblePasswordNotificationManager.createNotificationChannel(channel)
        }

        val bubblePasswordIntent = Intent(this, BubblePasswordActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val bubblePasswordPendingIntent = PendingIntent.getActivity(
            this,
            0,
            bubblePasswordIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bubblePasswordNotification = NotificationCompat.Builder(this, BUBBLE_PASSWORD_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_bubble_noti)
            .setAutoCancel(true)
            .setContentIntent(bubblePasswordPendingIntent)
            .build()

        bubblePasswordNotificationManager.notify(System.currentTimeMillis().toInt(), bubblePasswordNotification)
    }

    private fun bubblePasswordHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(BubblePasswordApp.BUBBLE_PASSWORD_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}