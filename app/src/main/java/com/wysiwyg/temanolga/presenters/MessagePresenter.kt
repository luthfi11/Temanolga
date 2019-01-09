package com.wysiwyg.temanolga.presenters

import android.content.Context
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Message
import com.wysiwyg.temanolga.utils.ConnectionUtil
import com.wysiwyg.temanolga.views.MessageView

class MessagePresenter(private val view: MessageView) {

    fun getMessage(ctx: Context?, msg: MutableList<Message>) {
        view.showLoading()
        FirebaseApi.getMessageList(msg, this)

        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        }
    }

    fun getMessageSuccess(msg: MutableList<Message>) {
        view.hideLoading()
        try {
            if (msg.size == 0) {
                view.showEmptyMessage()
            } else {
                view.showMessage()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}