package com.wysiwyg.temanolga.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var userId: String? = null,
    var fullName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var accountType: String? = null,
    var sportPreferred: String? = null,
    var city: String? = null,
    var imgPath: String? = null
) : Parcelable
