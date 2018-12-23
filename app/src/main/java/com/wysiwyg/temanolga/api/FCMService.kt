package com.wysiwyg.temanolga.api

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService: FirebaseMessagingService() {

    private val TAG = "Firebase Cloud"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage!!.notification != null) {
            Log.d(TAG, "Title: " + remoteMessage.notification?.title!!)
            Log.d(TAG, "Body: " + remoteMessage.notification?.body!!)
        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data: " + remoteMessage.data)
        }
    }

}