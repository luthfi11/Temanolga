package com.wysiwyg.temanolga.ui.message

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.chatroom.ChatRoomActivity
import com.wysiwyg.temanolga.ui.userdetail.UserDetailActivity
import com.wysiwyg.temanolga.data.network.FirebaseApi
import kotlinx.android.synthetic.main.item_message.view.*
import com.wysiwyg.temanolga.data.model.Message
import com.wysiwyg.temanolga.utilities.DateTimeUtils.isToday
import com.wysiwyg.temanolga.utilities.DateTimeUtils.isYesterday
import com.wysiwyg.temanolga.utilities.DateTimeUtils.dayAgo
import com.wysiwyg.temanolga.utilities.DateTimeUtils.timeFormat
import com.wysiwyg.temanolga.utilities.invisible
import com.wysiwyg.temanolga.utilities.visible
import org.jetbrains.anko.*

class MessageAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message,
                parent,
                false
            )
        )


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindItem(messages[i])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(message: Message) {
            var userChat = message.receiverId
            if (userChat == FirebaseApi.currentUser()) {
                userChat = message.senderId
            }

            FirebaseApi.getPostSender(userChat!!, itemView.tvUserMsg, null, itemView.imgUserMsg)

            if (isToday(message.timeStamp!!)) {
                itemView.tvTimeMsg.text = timeFormat("HH:mm", message.timeStamp)
            } else if (isYesterday(message.timeStamp)) {
                itemView.tvTimeMsg.text = dayAgo(message.timeStamp)
            } else {
                itemView.tvTimeMsg.text = timeFormat("dd/MM/yy", message.timeStamp)
            }

            itemView.tvContentMsg.text = message.msgContent

            itemView.setOnClickListener {
                itemView.context.startActivity<ChatRoomActivity>("userId" to userChat)
            }

            itemView.setOnLongClickListener {
                showMenu(userChat)
                true
            }

            if (message.read == false && message.senderId != FirebaseApi.currentUser()) {
                itemView.tvUserMsg.typeface = Typeface.DEFAULT_BOLD
                itemView.tvTimeMsg.typeface = Typeface.DEFAULT_BOLD
                itemView.tvContentMsg.typeface = Typeface.DEFAULT_BOLD
                itemView.imgMark.visible()
            } else {
                itemView.tvUserMsg.typeface = Typeface.DEFAULT
                itemView.tvTimeMsg.typeface = Typeface.DEFAULT
                itemView.tvContentMsg.typeface = Typeface.DEFAULT
                itemView.imgMark.invisible()
            }
        }

        private fun showMenu(userChat: String) {
            val menu = listOf("View Profile", "Chat", "Delete")
            itemView.context.selector(itemView.tvUserMsg.text, menu) { _, i ->
                when (i) {
                    0 -> itemView.context.startActivity<UserDetailActivity>("userId" to userChat)
                    1 -> itemView.context.startActivity<ChatRoomActivity>("userId" to userChat)
                    2 -> showConfirmDelete(userChat)
                }
            }
        }

        private fun showConfirmDelete(userChat: String) {
            itemView.context.alert(itemView.context.getString(R.string.delete_chat)) {
                yesButton { FirebaseApi.deleteChat(userChat) }
                noButton { it.dismiss() }
            }.show()
        }
    }
}
