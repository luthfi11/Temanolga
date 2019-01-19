package com.wysiwyg.temanolga.ui.explore

import android.content.Context
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class ExplorePresenter(private val view: ExploreView) {

    fun getUsers(ctx: Context?, user: MutableList<User>) {
        try {
            view.showLoading()
            FirebaseApi.getSuggestedUser(this, user)

            if (!ConnectionUtil.isOnline(ctx)) {
                view.hideLoading()
                view.showNoConnection()
            }
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