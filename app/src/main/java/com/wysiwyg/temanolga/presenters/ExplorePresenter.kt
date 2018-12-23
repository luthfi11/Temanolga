package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.views.ExploreView

class ExplorePresenter(private val view: ExploreView) {

    fun getUsers(user: MutableList<User>) {
        view.showLoading()
        FirebaseApi.getSuggestedUser(this, user)
    }

    fun getUsersSuccess() {
        view.hideLoading()
        view.showUsers()
    }
}