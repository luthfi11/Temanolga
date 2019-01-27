package com.wysiwyg.temanolga.ui.home

import android.content.Intent
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.data.model.Event
import kotlinx.android.synthetic.main.item_event.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.temanolga.ui.chatroom.ChatRoomActivity
import com.wysiwyg.temanolga.ui.editevent.EditEventActivity
import com.wysiwyg.temanolga.ui.eventdetail.EventDetailActivity
import com.wysiwyg.temanolga.ui.userdetail.UserDetailActivity
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.Join
import com.wysiwyg.temanolga.utilities.ConnectionUtil.isOnline
import com.wysiwyg.temanolga.utilities.DateTimeUtils.dateTimeFormat
import com.wysiwyg.temanolga.utilities.DateTimeUtils.minAgo
import com.wysiwyg.temanolga.utilities.SpinnerItem.slotType
import com.wysiwyg.temanolga.utilities.SpinnerItem.sportPref
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar

class EventAdapter(private val events: MutableList<Event>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_event,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindItem(events[i])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val database = FirebaseDatabase.getInstance().reference
        fun bindItem(event: Event) {

            var place = event.place
            if (place!!.contains(",")) {
                val shortPlace = event.place?.split(",")
                place = shortPlace!![0]
            }

            FirebaseApi.getPostSender(event.postSender!!, itemView.tvUser, null, itemView.imgUserMini)

            itemView.tvTimePost.text = minAgo(event.postTime!!)
            itemView.tvDay.text = dateTimeFormat(event.date, "dd")
            itemView.tvMonth.text = dateTimeFormat(event.date, "MMM yyyy")
            itemView.tvSport.text = sportPref(itemView.context, event.sportName)
            itemView.tvPlace.text = place
            itemView.tvUserCity.text = event.city

            if (event.description == "") {
                itemView.tvDesc.gone()
            } else {
                itemView.tvDesc.text = event.description
            }

            if (event.slot == null) {
                itemView.tvSlotJoin.text = String.format(
                    itemView.context.getString(R.string.joined_event),
                    event.slotFill, slotType(itemView.context, event.slotType)
                )
            } else {
                itemView.tvSlotJoin.text = String.format(
                    itemView.context.getString(R.string.joined_event_limited),
                    event.slotFill, event.slot, slotType(itemView.context, event.slotType)
                )
            }

            if (event.postSender != FirebaseApi.currentUser()) {
                itemView.imgUserMini.setOnClickListener {
                    itemView.context.startActivity<UserDetailActivity>("userId" to event.postSender)
                }
                itemView.tvUser.setOnClickListener {
                    itemView.context.startActivity<UserDetailActivity>("userId" to event.postSender)
                }
            }

            itemView.btnJoin.setOnClickListener {
                if (isOnline(itemView.context)) {
                    FirebaseApi.joinEvent(null, event.eventId, event.postSender)
                } else {
                    snackbar(itemView.tvDesc, itemView.context.getString(R.string.network_join))
                }
            }

            itemView.btnChat.setOnClickListener {
                itemView.context.startActivity<ChatRoomActivity>("userId" to event.postSender)
            }

            itemView.cvEvent.setOnClickListener {
                itemView.context.startActivity<EventDetailActivity>("eventId" to event.eventId)
            }

            itemView.btnEdit.setOnClickListener {
                itemView.context.startActivity<EditEventActivity>("event" to event)
            }

            itemView.btnDelete.setOnClickListener {
                itemView.context.alert(itemView.context.getString(R.string.delete_post)) {
                    yesButton {
                        if (isOnline(itemView.context)) {
                            FirebaseApi.deletePost(event.eventId!!)
                        } else {
                            snackbar(itemView.tvDesc, itemView.context.getString(R.string.network_delete))
                        }
                    }
                    noButton { it.dismiss() }
                }.show()
            }

            itemView.btnShare.setOnClickListener {
                val content = "${event.description} \n \n" +
                        "${sportPref(itemView.context, event.sportName)} at " +
                        "${event.place}, " +
                        "${event.date} ${event.time}"

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, content)
                intent.type = "text/plain"

                itemView.context.startActivity(Intent.createChooser(intent, "Share Invitation"))
            }

            if (event.postSender == FirebaseApi.currentUser()) {
                itemView.btnJoin.gone()
                itemView.btnChat.gone()
                itemView.btnEdit.visible()
                itemView.btnDelete.visible()
            } else {
                itemView.btnJoin.visible()
                itemView.btnChat.visible()
                itemView.btnEdit.gone()
                itemView.btnDelete.gone()
            }

            checkJoin(event.eventId)
            checkAccType(event.slotType!!)
            isFull(event.slotFill, event.slot)
            isExpire(event.date!!+", "+event.time)
        }

        private fun checkJoin(eventId: String?) {
            database.child("join")
                .child(eventId!!)
                .orderByChild("userReqId")
                .equalTo(FirebaseApi.currentUser()).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val join: MutableList<Join> = mutableListOf()
                            p0.children.mapNotNullTo(join) {
                                val data = it.getValue(Join::class.java)
                                when (data?.status) {
                                    "1" -> {
                                        joined()
                                        itemView.btnJoinAcc.showCancel(eventId, data.joinId!!)
                                    }
                                    "2" -> {
                                        requested()
                                        itemView.btnJoinReq.setOnClickListener {
                                            if (isOnline(itemView.context)) {
                                                FirebaseApi.cancelRequest(eventId, data.joinId!!)
                                                default()
                                            } else {
                                                snackbar(itemView.tvDesc, itemView.context.getString(R.string.network_join))
                                            }

                                        }
                                    }
                                    else -> default()
                                }
                                data
                            }
                        }
                    }
                })
        }


        private fun joined() {
            itemView.btnJoin.gone()
            itemView.btnJoinReq.gone()
            itemView.btnJoinAcc.visible()
        }

        private fun requested() {
            itemView.btnJoin.gone()
            itemView.btnJoinAcc.gone()
            itemView.btnJoinReq.visible()
        }

        private fun default() {
            itemView.btnJoin.visible()
            itemView.btnJoinAcc.gone()
            itemView.btnJoinReq.gone()
        }

        private fun View.showCancel(eventId: String, joinId: String) {
            setOnClickListener {
                itemView.context.alert(itemView.context.getString(R.string.cancel_join_prompt)) {
                    yesButton {
                        if (isOnline(itemView.context)) {
                            FirebaseApi.cancelJoin(eventId, joinId)
                            default()
                        } else {
                            snackbar(itemView.tvDesc, itemView.context.getString(R.string.network_joined))
                        }
                    }
                    noButton { it.dismiss() }
                }.show()
            }
        }

        private fun isFull(slotFill: Int?, slot: Int?) {
            if (slot != null && slotFill == slot) {
                val full = "$slotFill/$slot "+itemView.context.getString(R.string.slot_full)
                itemView.tvSlotJoin.textColorResource = R.color.colorBlack
                itemView.tvSlotJoin.typeface = Typeface.DEFAULT_BOLD
                itemView.tvSlotJoin.text = full
                itemView.btnJoin.isEnabled = false
                itemView.btnJoin.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorGrey))
            }
        }

        private fun isExpire(date: String) {
            val parseDate: Date = SimpleDateFormat("dd/MM/yy, HH : mm", Locale.getDefault()).parse(date)
            if (Date().after(parseDate)) {
                itemView.lytDate.backgroundResource = R.drawable.date_shape_expire
                itemView.btnJoin.isEnabled = false
                itemView.btnJoinAcc.isEnabled = false
                itemView.btnJoinReq.isEnabled = false

                itemView.btnJoin.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorGrey))
                itemView.btnJoinAcc.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorGrey))
                itemView.btnJoinReq.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorGrey))
            }
        }

        private fun checkAccType(slotType: String) {
            FirebaseDatabase.getInstance().reference.child("user")
                .child(FirebaseApi.currentUser()!!).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val accType = p0.child("accountType").getValue(String::class.java)
                    when (accType) {
                        "0" -> {
                            if (slotType == "1") {
                                disableJoin()
                            }
                        }
                        "1" -> {
                            if (slotType == "0") {
                                disableJoin()
                            }
                        }
                    }
                }
            })
        }

        private fun disableJoin() {
            itemView.btnJoin.isEnabled = false
            itemView.btnJoin.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorGrey))
        }
    }
}