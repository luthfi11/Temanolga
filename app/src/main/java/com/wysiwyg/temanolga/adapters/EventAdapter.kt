package com.wysiwyg.temanolga.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.models.Event
import kotlinx.android.synthetic.main.item_event.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.temanolga.activities.ChatRoomActivity
import com.wysiwyg.temanolga.activities.EditEventActivity
import com.wysiwyg.temanolga.activities.EventDetailActivity
import com.wysiwyg.temanolga.activities.UserDetailActivity
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Join
import com.wysiwyg.temanolga.utils.SpinnerItem.slotType
import com.wysiwyg.temanolga.utils.SpinnerItem.sportPref
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class EventAdapter(private val events: MutableList<Event>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false))
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

            val time: Long = event.postTime!!.toLong()
            val ago = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)

            var place = event.place
            if (place!!.contains(",")) {
                val shortPlace = event.place?.split(",")
                place = shortPlace!![0]
            }

            FirebaseApi.getPostSender(event.postSender!!, itemView.tvUser, null, itemView.imgUserMini)

            itemView.tvTimePost.text = ago
            itemView.tvDay.text = dateTimeFormat(event.date, "dd")
            itemView.tvMonth.text = dateTimeFormat(event.date, "MMM yyyy")
            itemView.tvSport.text = sportPref(itemView.context, event.sportName)
            itemView.tvPlace.text = place
            itemView.tvUserCity.text = event.city

            if (event.description == "") {
                itemView.tvDesc.visibility = View.GONE
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
                FirebaseApi.joinEvent(null, event.eventId, event.postSender)
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
                itemView.context.alert("Delete this post ?") {
                    yesButton { FirebaseApi.deletePost(event.eventId!!) }
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
                itemView.btnJoin.visibility = View.GONE
                itemView.btnChat.visibility = View.GONE
                itemView.btnEdit.visibility = View.VISIBLE
                itemView.btnDelete.visibility = View.VISIBLE
            } else {
                itemView.btnJoin.visibility = View.VISIBLE
                itemView.btnChat.visibility = View.VISIBLE
                itemView.btnEdit.visibility = View.GONE
                itemView.btnDelete.visibility = View.GONE
            }

            checkJoin(event.eventId)
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
                                        itemView.btnJoinReq.cancel(eventId, data.joinId!!)
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
            itemView.btnJoin.visibility = View.GONE
            itemView.btnJoinReq.visibility = View.GONE
            itemView.btnJoinAcc.visibility = View.VISIBLE
        }

        private fun requested() {
            itemView.btnJoin.visibility = View.GONE
            itemView.btnJoinAcc.visibility = View.GONE
            itemView.btnJoinReq.visibility = View.VISIBLE
        }

        private fun default() {
            itemView.btnJoin.visibility = View.VISIBLE
            itemView.btnJoinAcc.visibility = View.GONE
            itemView.btnJoinReq.visibility = View.GONE
        }

        private fun View.showCancel(eventId: String, joinId: String) {
            setOnClickListener {
                itemView.context.alert("Cancel joined this invitation ?") {
                    yesButton {
                        FirebaseApi.cancelJoin(eventId, joinId)
                        default()
                    }
                    noButton { it.dismiss() }
                }.show()
            }
        }

        private fun View.cancel(eventId: String, joinId: String) {
            setOnClickListener {
                FirebaseApi.cancelJoin(eventId, joinId)
                default()
            }
        }

        private fun dateTimeFormat(date: String?, pattern: String): String {
            val format = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())

            return sdf.format(format.parse(date))
        }
    }
}