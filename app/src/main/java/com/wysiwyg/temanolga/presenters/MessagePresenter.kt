package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Message
import com.wysiwyg.temanolga.views.MessageView

class MessagePresenter(private val view: MessageView) {

    fun getMessage(msg: MutableList<Message>) {
        view.showLoading()
        FirebaseApi.getMessageList(msg, this)
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