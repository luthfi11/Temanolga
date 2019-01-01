package com.wysiwyg.temanolga.utils

import android.content.Context
import android.net.ConnectivityManager
import java.util.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ConnectionUtil {

    fun isOnline(ctx: Context): Boolean {
        var con = false

        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo

        if (netInfo != null && netInfo.isConnected) {
            con = true
        }

        return con
    }

    fun loadTimeout(listener: () -> Unit) {
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                listener()
            }
        }
        timer.schedule(timerTask, 3000)
    }

    fun check(listener: () -> Unit) {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


    }

}