package com.wysiwyg.temanolga.adapters

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.activities.ChatRoomActivity
import com.wysiwyg.temanolga.activities.UserDetailActivity
import com.wysiwyg.temanolga.api.FirebaseApi
import kotlinx.android.synthetic.main.item_message.view.*
import com.wysiwyg.temanolga.models.Message
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false))


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

            if (DateUtils.isToday(message.timeStamp!!.toLong())) {
                itemView.tvTimeMsg.text = timeFormat("HH:mm", message.timeStamp)
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
            itemView.context.alert("Delete this chat ?") {
                yesButton { FirebaseApi.deleteChat(userChat) }
                noButton { it.dismiss() }
            }.show()
        }

        private fun timeFormat(format: String, millis: String): String {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(Date(millis.toLong()))
        }
    }
}
