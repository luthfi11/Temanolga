package com.wysiwyg.temanolga.ui.message

import android.content.Context
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.Message
import com.wysiwyg.temanolga.utilities.ConnectionUtil

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