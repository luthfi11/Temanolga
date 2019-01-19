package com.wysiwyg.temanolga.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Join (
    var joinId: String? = null,
    var eventId: String? = null,
    var postSender: String? = null,
    var userReqId: String? = null,
    var status: String? = null,
    var timeStamp: String? = null,
    var confirmTS: String? = null
) : Parcelable