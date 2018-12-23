package com.wysiwyg.temanolga.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Message
import kotlinx.android.synthetic.main.item_chat_room.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false))


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindItem(messages[i])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(message: Message) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time : String = sdf.format(Date(message.timeStamp!!.toLong()))

            if (message.senderId == FirebaseApi.currentUser()) {
                itemView.tvMessage.text = message.msgContent
                itemView.tvSentTime.text = time
                itemView.view1.visibility = View.GONE
            } else {
                FirebaseApi.getPostSender(message.senderId!!, null, null, itemView.imgSender)
                itemView.tvMessage1.text = message.msgContent
                itemView.tvSentTime1.text = time
                itemView.view.visibility = View.GONE
            }

        }
    }
}
