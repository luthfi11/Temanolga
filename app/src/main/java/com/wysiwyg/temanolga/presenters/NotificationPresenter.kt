package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Join
import com.wysiwyg.temanolga.views.NotificationView

class NotificationPresenter(private val view: NotificationView) {

    fun getConfirm(join: MutableList<Join>) {
        view.showLoading()
        FirebaseApi.getConfirmNotif(this, join)
    }

    fun getRequest(join: MutableList<Join>) {
        view.showLoading()
        FirebaseApi.getRequestNotif(this, join)
    }

    fun getNotifSuccess() {
        view.hideLoading()
        view.showNotification()
    }
}