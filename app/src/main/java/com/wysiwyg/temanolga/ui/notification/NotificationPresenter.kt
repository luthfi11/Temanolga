package com.wysiwyg.temanolga.ui.notification

import android.content.Context
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.Join
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class NotificationPresenter(private val view: NotificationView) {

    fun getConfirm(join: MutableList<Join>) {
        view.showLoading()
        FirebaseApi.getConfirmNotif(this, join)
    }

    fun getRequest(ctx: Context?, join: MutableList<Join>) {
        view.showLoading()
        FirebaseApi.getRequestNotif(this, join)

        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        }
    }

    fun getNotifSuccess(join: MutableList<Join>) {
        view.hideLoading()
        try {
            if (join.size == 0) {
                view.showEmptyNotif()
            } else {
                view.showNotification()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}