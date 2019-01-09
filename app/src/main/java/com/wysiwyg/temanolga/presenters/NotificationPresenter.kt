package com.wysiwyg.temanolga.presenters

import android.content.Context
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Join
import com.wysiwyg.temanolga.utils.ConnectionUtil
import com.wysiwyg.temanolga.views.NotificationView

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