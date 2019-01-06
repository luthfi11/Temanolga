package com.wysiwyg.temanolga.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wysiwyg.temanolga.R

class FirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val id = remoteMessage.data["from_user_id"]
        val message = remoteMessage.notification?.body
        val name = remoteMessage.notification?.title
        val action = remoteMessage.notification?.clickAction

        if (FirebaseAuth.getInstance().currentUser != null){
            sendNotification(message, name, action!!, id)
        }
    }

    private fun sendNotification(messageBody: String?, name: String?, target: String, id: String?) {
        val intent = Intent(this, Class.forName(target))
        if (id != null) {
            intent.putExtra("userId", id)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(name)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_logo_round))
            .setSmallIcon(R.drawable.ic_logo_app)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Temanolga",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}