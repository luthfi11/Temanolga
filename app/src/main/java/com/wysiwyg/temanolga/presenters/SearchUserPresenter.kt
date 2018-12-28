package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.views.SearchUserView

class SearchUserPresenter(private val view: SearchUserView) {

    fun getData(user: MutableList<User?>, name: String) {
        view.showLoading()
        FirebaseApi.searchUser(this, user, name)
    }

    fun getDataSuccess(user: MutableList<User?>) {
        view.hideLoading()
        try {
            if (user.size == 0) {
                view.showUserNotFound()
            } else {
                view.showUser()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}