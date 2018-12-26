package com.wysiwyg.temanolga.presenters

import android.os.Bundle
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.views.EventDetailView

class EventDetailPresenter(private val view: EventDetailView) {

    fun getData(eventId: String, event: MutableList<Event>) {
        FirebaseApi.getEventDetail(eventId, this, event)
    }

    fun getDataSuccess() {
        view.showEventData()
    }

    fun getMap(savedInstanceState: Bundle?, long: String?, lat: String?, title: String?) {
        view.showMap(savedInstanceState, long, lat, title)
    }

    fun hideMap() {
        view.hideMap()
    }

    fun joinEvent(eventId: String?, postSender: String?) {
        FirebaseApi.joinEvent(this, eventId, postSender)
    }

    fun joinSuccess() {
        view.showJoinMsg()
    }

    fun checkJoin(eventId: String?) {
        FirebaseApi.checkJoin(this, eventId)
    }

    fun isRequested(joinId: String) {
        view.showRequested(joinId)
    }

    fun isJoin(joinId: String) {
        view.showJoined(joinId)
    }

    fun defaultJoin(){
        view.showDefJoin()
    }

    fun cancelConfirm(joinId: String) {
        view.showCancelJoin(joinId)
    }

    fun cancelJoin(eventId: String, joinId: String){
        FirebaseApi.cancelJoin(eventId, joinId)
        view.showDefJoin()
    }

    fun deleteConfirm() {
        view.showDeleteConfirm()
    }

    fun delete(eventId: String) {
        FirebaseApi.deletePost(eventId)
        view.afterDelete()
    }

    fun isExpire(date: String) {
        view.showExpire(date)
    }
}