package com.wysiwyg.temanolga.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.eventdetail.EventDetailActivity
import com.wysiwyg.temanolga.ui.userdetail.UserDetailActivity
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.Join
import com.wysiwyg.temanolga.utilities.DateTimeUtils.minAgo
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.item_notification.view.*
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textResource
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val notif: MutableList<Join>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_notification,
                parent,
                false
            )
        )

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
                    itemView.vBtnNotif.gone()
                    itemView.vTvStatus.visible()
                    itemView.tvStatus.text = itemView.context.getString(R.string.ignored)
                }
                "1" -> {
                    itemView.vBtnNotif.gone()
                    itemView.vTvStatus.visible()
                    itemView.tvStatus.text = itemView.context.getString(R.string.accepted)
                    itemView.tvStatus.textColorResource = R.color.colorAccent
                }
            }

            if (notif.postSender == FirebaseApi.currentUser()) {
                FirebaseApi.getPostSender(notif.userReqId!!, null, null, itemView.imgUserNotif)
                FirebaseApi.getNotifDetail(itemView.tvReqNotif, notif.eventId!!, notif.userReqId!!, notif.joinId!!, itemView.context)

                itemView.tvTimeNotif.text = minAgo(notif.timeStamp!!)

                itemView.imgUserNotif.setOnClickListener {
                    itemView.context.startActivity<UserDetailActivity>("userId" to notif.userReqId)
                }

                itemView.btnAccept.setOnClickListener { FirebaseApi.acceptRequest(notif.eventId!!, notif.joinId!!) }
                itemView.btnIgnore.setOnClickListener { FirebaseApi.ignoreRequest(notif.eventId!!, notif.joinId!!) }

                itemView.cvNotif.gone()

                if (notif.status == "2") {
                    isExpire(notif.eventId!!)
                }

            } else if((notif.postSender != FirebaseApi.currentUser()) and (notif.status == "2")) {
                itemView.cvNotif.gone()
            }

            if (notif.userReqId == FirebaseApi.currentUser()) {
                FirebaseApi.getPostSender(notif.postSender!!, null, null, itemView.imgUserNotif2)
                FirebaseApi.getNotifDetail(itemView.tvNotif, notif.eventId!!, notif.postSender!!, notif.joinId!!, itemView.context)

                itemView.tvTimeNotif2.text = minAgo(notif.confirmTS!!)

                itemView.cvReq.gone()
            }

            itemView.setOnClickListener { itemView.context.startActivity<EventDetailActivity>("eventId" to notif.eventId) }
        }

        private fun isExpire(eventId: String) {
            itemView.vTvExpire.gone()
            FirebaseDatabase.getInstance().reference.child("event").child(eventId).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val date = p0.child("date").getValue(String::class.java)
                    val time = p0.child("time").getValue(String::class.java)
                    val slotFill = p0.child("slotFill").getValue(Int::class.java)
                    val slot = p0.child("slot").getValue(Int::class.java)

                    val parseDate: Date = SimpleDateFormat("dd/MM/yyy, HH : mm", Locale.getDefault()).parse(date+", "+time)
                    if (Date().after(parseDate)) {
                        itemView.vBtnNotif.gone()
                        itemView.vTvExpire.visible()
                    } else if (slotFill == slot && slot != null) {
                        itemView.vBtnNotif.gone()
                        itemView.vTvExpire.visible()
                        itemView.tvExpire.textResource = R.string.slot_full
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }
}
