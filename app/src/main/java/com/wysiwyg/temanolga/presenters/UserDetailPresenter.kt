package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.views.UserDetailView

class UserDetailPresenter(private val view: UserDetailView) {

    fun getUser(user: MutableList<User>, uid: String) {
        view.showLoading()
        FirebaseApi.getUserDetail(user, uid,this)
    }

    fun getUserSuccess(user: MutableList<User>) {
        view.hideLoading()
        view.showUserData(user)
    }

    fun getUserEvent(events: MutableList<Event>, uid: String) {
        FirebaseApi.getUserDetailEvent(events, uid, this)
    }

    fun getUserEventSuccess(events: MutableList<Event>) {
        if (events.size == 0) {
            view.showEmptyPost()
        } else {
            view.showEventData()
        }
    }
}