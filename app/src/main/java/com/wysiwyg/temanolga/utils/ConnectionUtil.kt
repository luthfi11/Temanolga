package com.wysiwyg.temanolga.utils

import android.content.Context
import android.net.ConnectivityManager

object ConnectionUtil {

    fun isOnline(ctx: Context?): Boolean {
        val cm = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo

        return (netInfo != null && netInfo.isConnected)
    }
}