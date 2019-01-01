package com.wysiwyg.temanolga.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.activities.ChatRoomActivity
import org.jetbrains.anko.toast
import java.lang.Exception

class FirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val message = data["msgContent"]
        val id = data["userId"]
        val name = data["name"]
        if (FirebaseAuth.getInstance().currentUser != null){
            sendNotification(message.toString(), id.toString(), name.toString())
        }
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)
        toast("Message sent $p0").show()
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        super.onSendError(p0, p1)
        toast("Error ${p1?.stackTrace}").show()
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

        sendRegistrationToServer(p0)
    }

    private fun sendNotification(messageBody: String, id: String, name: String) {
        val intent = Intent(this, ChatRoomActivity::class.java)
        intent.putExtra("userId", id)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(name)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendRegistrationToServer(token: String?) {

    }
}