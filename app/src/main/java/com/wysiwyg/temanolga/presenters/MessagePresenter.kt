package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Message
import com.wysiwyg.temanolga.views.MessageView

class MessagePresenter(private val view: MessageView) {

    fun getMessage(msg: MutableList<Message>) {
        view.showLoading()
        FirebaseApi.getMessageList(msg, this)
    }

    fun getMessageSuccess() {
        view.hideLoading()
        view.showMessage()
    }

}