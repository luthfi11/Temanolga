package com.wysiwyg.temanolga.adapters

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.activities.EventDetailActivity
import com.wysiwyg.temanolga.activities.UserDetailActivity
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Join
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.item_notification.view.*
import org.jetbrains.anko.textColorResource

class NotificationAdapter(private val notif: MutableList<Join>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindItem(notif[i])
    }

    override fun getItemCount(): Int {
        return notif.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(notif: Join) {
            when(notif.status) {
                "0" -> {
                    itemView.vBtnNotif.visibility = View.GONE
                    itemView.vTvStatus.visibility = View.VISIBLE
                    itemView.tvStatus.text = itemView.context.getString(R.string.ignored)
                }
                "1" -> {
                    itemView.vBtnNotif.visibility = View.GONE
                    itemView.vTvStatus.visibility = View.VISIBLE
                    itemView.tvStatus.text = itemView.context.getString(R.string.accepted)
                    itemView.tvStatus.textColorResource = R.color.colorAccent
                }
            }

            if (notif.postSender == FirebaseApi.currentUser()) {
                FirebaseApi.getPostSender(notif.userReqId!!, null, null, itemView.imgUserNotif)
                FirebaseApi.getNotifDetail(itemView.tvReqNotif, notif.eventId!!, notif.userReqId!!, notif.joinId!!, itemView.context)

                val ago = DateUtils.getRelativeTimeSpanString(notif.timeStamp!!.toLong(),
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)

                itemView.tvTimeNotif.text = ago

                itemView.imgUserNotif.setOnClickListener {
                    itemView.context.startActivity<UserDetailActivity>("userId" to notif.userReqId)
                }

                itemView.btnAccept.setOnClickListener { FirebaseApi.acceptRequest(notif.eventId!!, notif.joinId!!) }
                itemView.btnIgnore.setOnClickListener { FirebaseApi.ignoreRequest(notif.eventId!!, notif.joinId!!) }

                itemView.cvNotif.visibility = View.GONE

            } else if((notif.postSender != FirebaseApi.currentUser()) and (notif.status == "2")) {
                itemView.cvNotif.visibility = View.GONE
            }

            if (notif.userReqId == FirebaseApi.currentUser()) {
                FirebaseApi.getPostSender(notif.postSender!!, null, null, itemView.imgUserNotif2)
                FirebaseApi.getNotifDetail(itemView.tvNotif, notif.eventId!!, notif.postSender!!, notif.joinId!!, itemView.context)

                val conAgo = DateUtils.getRelativeTimeSpanString(notif.confirmTS!!.toLong(),
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
                itemView.tvTimeNotif2.text = conAgo

                itemView.cvReq.visibility = View.GONE
            }

            itemView.setOnClickListener { itemView.context.startActivity<EventDetailActivity>("eventId" to notif.eventId) }
        }
    }
}
