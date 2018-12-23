package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.views.ProfileView

class ProfilePresenter(private val view: ProfileView) {

    fun getUser(user: MutableList<User>) {
        view.showLoading()
        FirebaseApi.getCurrentUserData(user,this)
    }

    fun getUserSuccess(user: MutableList<User>) {
        view.hideLoading()
        view.showUserData(user)
    }

    fun getUserEvent(events: MutableList<Event>) {
        FirebaseApi.getUserEventData(events, this)
    }

    fun getUserEventSuccess() {
        view.showEventData()
    }

    fun logOut() {
        FirebaseApi.logOut()
        view.doLogOut()
    }
}