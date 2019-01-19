package com.wysiwyg.temanolga.ui.searchuser

import android.content.Context
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class SearchUserPresenter(private val view: SearchUserView) {

    fun getData(ctx: Context?, user: MutableList<User?>, name: String) {
        try {
            view.showLoading()
            FirebaseApi.searchUser(this, user, name)

            if (!ConnectionUtil.isOnline(ctx)) {
                view.hideLoading()
                view.showNoConnection()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
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