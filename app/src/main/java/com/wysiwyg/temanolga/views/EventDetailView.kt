package com.wysiwyg.temanolga.views

import android.os.Bundle

interface EventDetailView {
    fun showEventData()
    fun showMap(savedInstanceState: Bundle?, long: String?, lat: String?, title: String?)
    fun hideMap()
    fun showJoinMsg()
    fun showRequested(joinId: String)
    fun showJoined(joinId: String)
    fun showDefJoin()
    fun showCancelJoin(joinId: String)
    fun showDeleteConfirm()
    fun isOwnPost()
    fun isUserPost()
    fun disableJoin(slotType: String)
    fun afterDelete()
    fun showExpire(date: String)
}