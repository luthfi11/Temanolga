package com.wysiwyg.temanolga.ui.chatroom

import android.widget.ImageView
import android.widget.TextView
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.Message

class ChatRoomPresenter(private val view: ChatRoomView) {

    fun getReceiver(receiver: String, tvUserMsgRoom: TextView, imgUserRoom: ImageView) {
        FirebaseApi.getPostSender(receiver, tvUserMsgRoom, null, imgUserRoom)
    }

    fun getMessage(receiver: String, msg: MutableList<Message>) {
        FirebaseApi.getMessage(receiver, msg, this)
    }

    fun getMessageSuccess() {
        view.showMessage()
    }

    fun setRead(userId: String) {
        FirebaseApi.setReadMessage(userId)
    }

    fun sendMessage(receiver: String, message: String) {
        FirebaseApi.sendMessage(receiver, message, this)
    }

    fun sendFailed() {
        view.showFail()
    }
}