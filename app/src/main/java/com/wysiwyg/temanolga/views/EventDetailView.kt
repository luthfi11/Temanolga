package com.wysiwyg.temanolga.views

import android.os.Bundle

interface EventDetailView {
    fun showEventData()
    fun showMap(savedInstanceState: Bundle?, long: String?, lat: String?, title: String?)
    fun hideMap()
    fun showJoinMsg()
    fun showRequested()
    fun showJoined()
    fun showDeleteConfirm()
    fun afterDelete()
}