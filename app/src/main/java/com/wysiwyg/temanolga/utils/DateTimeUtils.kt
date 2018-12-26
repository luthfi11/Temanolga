package com.wysiwyg.temanolga.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    fun timeFormat(format: String, millis: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date(millis.toLong()))
    }

    fun minAgo(millis: String): String {
        return DateUtils.getRelativeTimeSpanString(
            millis.toLong(),
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }

    fun dayAgo(millis: String): String {
        return DateUtils.getRelativeTimeSpanString(
            millis.toLong(),
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS
        ).toString()
    }

    fun isToday(millis: String): Boolean {
        return DateUtils.isToday(millis.toLong())
    }

    fun isYesterday(millis: String): Boolean {
        return DateUtils.isToday(millis.toLong() + DateUtils.DAY_IN_MILLIS)
    }

    fun dateTimeFormat(date: String?, pattern: String): String {
        val format = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())

        return sdf.format(format.parse(date))
    }
}