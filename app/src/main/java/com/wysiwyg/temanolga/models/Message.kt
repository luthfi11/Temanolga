package com.wysiwyg.temanolga.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    val messageId: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val msgContent: String? = null,
    val timeStamp: String? = null
) : Parcelable