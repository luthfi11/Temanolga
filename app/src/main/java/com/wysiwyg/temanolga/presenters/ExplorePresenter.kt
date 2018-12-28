package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.views.ExploreView

class ExplorePresenter(private val view: ExploreView) {

    fun getUsers(user: MutableList<User>) {
        try {
            view.showLoading()
            FirebaseApi.getSuggestedUser(this, user)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getUsersSuccess(user: MutableList<User>) {
        try {
            view.hideLoading()
            if (user.size == 0) {
                view.showEmptyUser()
            } else {
                view.showUsers()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}