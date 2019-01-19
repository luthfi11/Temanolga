package com.wysiwyg.temanolga.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var eventId: String? = null,
    var postSender: String? = null,
    var sportName: String? = null,
    var place: String? = null,
    var city: String? = null,
    var date: String? = null,
    var time: String? = null,
    var slot: Int? = null,
    var slotType: String? = null,
    var slotFill: Int? = null,
    var description: String? = null,
    var postTime: String? = null,
    var longLat: String? = null
) : Parcelable
