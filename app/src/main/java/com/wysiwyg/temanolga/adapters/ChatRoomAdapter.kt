package com.wysiwyg.temanolga.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Message
import kotlinx.android.synthetic.main.item_chat_room.view.*
import org.jetbrains.anko.selector
import java.text.SimpleDateFormat
import java.util.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.wysiwyg.temanolga.utils.DateTimeUtils.dayAgo
import com.wysiwyg.temanolga.utils.gone
import com.wysiwyg.temanolga.utils.invisible
import com.wysiwyg.temanolga.utils.visible

class ChatRoomAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (i > 0) {
            if (dayAgo(messages[i-1].timeStamp!!) != dayAgo(messages[i].timeStamp!!)) {
                viewHolder.bindTime(messages[i])
            } else {
                viewHolder.hideTime()
            }
        }
        if((i == 0) && (messages.size != 0)) {
            viewHolder.bindTime(messages[0])
        }
        viewHolder.bindItem(messages[i])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(message: Message) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time: String = sdf.format(Date(message.timeStamp!!.toLong()))

            if (message.senderId == FirebaseApi.currentUser()) {
                itemView.tvMessage.text = message.msgContent
                itemView.tvSentTime.text = time
                itemView.view1.gone()
                itemView.view.visible()
                itemView.cv.copyToClipboard(message.msgContent)

                if (message.read == true) {
                    itemView.tvRead.visible()
                    itemView.tvRead.text = itemView.context.getString(R.string.read)
                } else {
                    itemView.tvRead.invisible()
                }

            } else {
                FirebaseApi.getPostSender(message.senderId!!, null, null, itemView.imgSender)
                itemView.tvMessage1.text = message.msgContent
                itemView.tvSentTime1.text = time
                itemView.view.gone()
                itemView.view1.visible()
                itemView.cv1.copyToClipboard(message.msgContent)
            }
        }

        fun bindTime(message: Message) {
            itemView.tvTime.visible()
            itemView.tvTime.text = dayAgo(message.timeStamp!!)
        }

        fun hideTime() {
            itemView.tvTime.gone()
        }

        private fun View.copyToClipboard(msg: String?) {
            setOnLongClickListener {
                val menu = listOf("Copy Text")
                itemView.context.selector(null, menu) { _, i ->
                    when (i) {
                        0 -> {
                            val clipboard =
                                itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("", msg)
                            clipboard.primaryClip = clip
                        }
                    }
                }
                true
            }
        }
    }
}
