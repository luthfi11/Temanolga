package com.wysiwyg.temanolga.presenters

import android.widget.ImageView
import android.widget.TextView
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Message
import com.wysiwyg.temanolga.views.ChatRoomView

class ChatRoomPresenter(private val view: ChatRoomView) {

    fun getReceiver(receiver: String, tvUserMsgRoom: TextView, tvUserCityRoom: TextView, imgUserRoom: ImageView) {
        FirebaseApi.getPostSender(receiver, tvUserMsgRoom, tvUserCityRoom, imgUserRoom)
    }

    fun getMessage(receiver: String, msg: MutableList<Message>) {
        FirebaseApi.getMessage(receiver, msg, this)
    }

    fun getMessageSuccess() {
        view.showMessage()
    }

    fun sendMessage(receiver: String, message: String) {
        FirebaseApi.sendMessage(receiver, message, this)
    }

    fun sendFailed() {
        view.showFail()
    }
}