package com.wysiwyg.temanolga.data.network

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.data.model.Event
import com.wysiwyg.temanolga.data.model.Message
import com.wysiwyg.temanolga.data.model.User
import java.util.Collections.reverse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.wysiwyg.temanolga.data.model.Join
import com.wysiwyg.temanolga.ui.addevent.AddEventPresenter
import com.wysiwyg.temanolga.ui.chatroom.ChatRoomPresenter
import com.wysiwyg.temanolga.ui.editevent.EditEventPresenter
import com.wysiwyg.temanolga.ui.editprofile.EditProfilePresenter
import com.wysiwyg.temanolga.ui.eventdetail.EventDetailPresenter
import com.wysiwyg.temanolga.ui.explore.ExplorePresenter
import com.wysiwyg.temanolga.ui.home.HomePresenter
import com.wysiwyg.temanolga.ui.login.LoginPresenter
import com.wysiwyg.temanolga.ui.message.MessagePresenter
import com.wysiwyg.temanolga.ui.notification.NotificationPresenter
import com.wysiwyg.temanolga.ui.profile.ProfilePresenter
import com.wysiwyg.temanolga.ui.searchuser.SearchUserPresenter
import com.wysiwyg.temanolga.ui.signup.SignUpPresenter
import com.wysiwyg.temanolga.ui.userdetail.UserDetailPresenter
import com.wysiwyg.temanolga.utilities.Spannable.setBoldSpannable
import com.wysiwyg.temanolga.utilities.SpinnerItem.sportPref
import java.util.*

object FirebaseApi {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference
    private const val imgPath =
        "https://firebasestorage.googleapis.com/v0/b/temanolahraga-uno.appspot.com/o/ic_logo_app.png?alt=media&token=525ec5bc-6e97-4684-acdd-cd59d5fc8a20"

    fun currentUser(): String? {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun login(email: String, password: String, presenter: LoginPresenter) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                presenter.loginSuccess()

                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(object :
                    OnSuccessListener<InstanceIdResult> {
                    override fun onSuccess(p0: InstanceIdResult?) {
                        val token = p0?.token.toString()
                        database.child("user")
                            .child(auth.currentUser!!.uid)
                            .child("tokenId")
                            .setValue(token)
                    }
                })

            } else {
                presenter.loginFailed()
            }
        }
    }

    fun signUp(
        fullName: String, email: String, password: String, accountType: String,
        sport: String, city: String, presenter: SignUpPresenter
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                presenter.signUpSuccess()
                addUserData(
                    fullName,
                    email,
                    password,
                    accountType,
                    sport,
                    city
                )
            } else {
                presenter.signUpFailed()
            }
        }
    }

    private fun addUserData(
        fullName: String?, email: String?, password: String?,
        accountType: String?, sport: String?, city: String?
    ) {
        database.child("user")
            .child(auth.currentUser!!.uid)
            .setValue(
                User(
                    auth.currentUser!!.uid,
                    fullName,
                    email,
                    password,
                    accountType,
                    sport,
                    city,
                    imgPath
                )
            )
    }

    fun logOut() {
        database.child("user")
            .child(auth.currentUser!!.uid)
            .child("tokenId")
            .setValue("")
        auth.signOut()
    }

    fun getEventData(events: MutableList<Event>, sport: String, city: String, presenter: HomePresenter) {
        database.child("event").orderByChild("sportName").equalTo(sport)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    events.clear()

                    for (data: DataSnapshot in dataSnapshot.children) {
                        val eve = data.getValue(Event::class.java)
                        if (eve?.city?.contains(city, true)!!) {
                            events.add(eve)
                        }
                    }

                    reverse(events)
                    presenter.getDataSuccess(events)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun getUserEventData(events: MutableList<Event>, presenter: ProfilePresenter) {
        database.child("event").orderByChild("postSender").equalTo(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    events.clear()
                    dataSnapshot.children.mapNotNullTo(events) {
                        it.getValue<Event>(Event::class.java)
                    }
                    reverse(events)
                    presenter.getUserEventSuccess(events)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun addEventData(
        presenter: AddEventPresenter, sport: String?, place: String?, date: String?, time: String?,
        slot: Int?, slotType: String?, desc: String?, longLat: String?
    ) {
        database.child("user").child(auth.currentUser!!.uid).child("city")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val city = p0.getValue(String::class.java)

                    val eventId = database.child("event").push().key
                    database.child("event")
                        .child(eventId!!)
                        .setValue(
                            Event(
                                eventId, auth.currentUser!!.uid,
                                sport, place, city, date, time, slot, slotType, 0, desc,
                                System.currentTimeMillis().toString(), longLat
                            )
                        )
                        .addOnCompleteListener {
                            presenter.postSuccess()
                        }
                        .addOnFailureListener {
                            presenter.postFail()
                        }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    fun getEventDetail(eventId: String, presenter: EventDetailPresenter, event: MutableList<Event>) {
        database.child("event").orderByChild("eventId").equalTo(eventId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    event.clear()
                    p0.children.mapNotNullTo(event) {
                        it.getValue<Event>(Event::class.java)
                    }
                    presenter.getDataSuccess()
                }
            })

    }

    fun getCurrentUserData(userData: MutableList<User>, presenter: ProfilePresenter) {
        database.child("user").orderByChild("userId").equalTo(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userData.clear()
                    dataSnapshot.children.mapNotNullTo(userData) {
                        it.getValue<User>(User::class.java)
                    }

                    presenter.getUserSuccess(userData)
                }
            })
    }

    fun getPostSender(userId: String, tvUser: TextView?, tvCity: TextView?, imgUser: ImageView?) {
        database.child("user").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val img = p0.child("imgPath").getValue(String::class.java)
                Picasso.get().load(img).resize(200, 200).centerCrop()
                    .placeholder(R.color.colorMuted).into(imgUser)
                val fullName = p0.child("fullName").getValue(String::class.java)
                tvUser?.text = fullName
                val city = p0.child("city").getValue(String::class.java)
                tvCity?.text = city
            }
        })
    }

    fun getUserSportPreferred(spnSport: Spinner?, spnSlot: Spinner?) {
        database.child("user")
            .child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val sportPref = p0.child("sportPreferred").getValue(String::class.java)
                    spnSport?.setSelection(sportPref!!.toInt())

                    val accType = p0.child("accountType").getValue(String::class.java)
                    spnSlot?.setSelection(accType!!.toInt())
                }
            })
    }

    fun uploadPhoto(filePath: Uri, presenter: EditProfilePresenter) {
        val fileRef = storage.child(auth.currentUser!!.uid + ".png")
        var value: Double

        fileRef.putFile(filePath).addOnProgressListener { taskSnapshot ->
            value = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            presenter.uploadProgress(value)

        }.continueWithTask { uploadTask ->
            if (!uploadTask.isSuccessful) {
                throw uploadTask.exception!!
            }
            return@continueWithTask fileRef.downloadUrl

        }.addOnSuccessListener { uri ->
            presenter.progressComplete(uri.toString())
            database.child("user")
                .child(auth.currentUser!!.uid)
                .child("imgPath")
                .setValue(uri.toString())
        }
    }

    fun sendMessage(receiver: String, message: String, presenter: ChatRoomPresenter) {
        val msgId = database.child("message").push().key
        database.child("message")
            .child(auth.currentUser!!.uid)
            .child(receiver)
            .child(msgId!!)
            .setValue(
                Message(
                    msgId,
                    auth.currentUser!!.uid,
                    receiver,
                    message,
                    System.currentTimeMillis().toString()
                )
            )
            .addOnFailureListener {
                presenter.sendFailed()
            }

        database.child("message")
            .child(receiver)
            .child(auth.currentUser!!.uid)
            .child(msgId)
            .setValue(
                Message(
                    msgId,
                    auth.currentUser!!.uid,
                    receiver,
                    message,
                    System.currentTimeMillis().toString()
                )
            )
    }

    fun getMessage(receiver: String, msg: MutableList<Message>, presenter: ChatRoomPresenter) {
        database.child("message")
            .child(auth.currentUser!!.uid)
            .child(receiver)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    msg.clear()
                    p0.children.mapNotNullTo(msg) {
                        it.getValue<Message>(Message::class.java)
                    }
                    presenter.getMessageSuccess()
                }
            })
    }

    fun setReadMessage(userId: String) {
        val ref1 = database.child("message").child(auth.currentUser?.uid!!).child(userId)
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data: DataSnapshot in p0.children) {
                    val id = data.child("senderId").getValue(String::class.java)
                    if (id != auth.currentUser?.uid) {
                        val msgId = data.child("messageId").getValue(String::class.java)!!
                        ref1.child(msgId).child("read").setValue(true)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        val ref2 = database.child("message").child(userId).child(auth.currentUser?.uid!!)
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data: DataSnapshot in p0.children) {
                    val id = data.child("receiverId").getValue(String::class.java)
                    if (id == auth.currentUser?.uid) {
                        val msgId = data.child("messageId").getValue(String::class.java)!!
                        ref2.child(msgId).child("read").setValue(true)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun getMessageList(msg: MutableList<Message>, presenter: MessagePresenter) {
        database.child("message")
            .child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(chatSnapshot: DataSnapshot) {
                    msg.clear()
                    var d: Message? = null
                    for (dt: DataSnapshot in chatSnapshot.children) {
                        for (dt2: DataSnapshot in dt.children) {
                            d = dt2.getValue(Message::class.java)
                        }
                        msg.add(d!!)
                    }
                    Collections.sort(msg, object : Comparator<Message> {
                        override fun compare(o1: Message?, o2: Message?): Int {
                            return o2?.timeStamp!!.compareTo(o1?.timeStamp!!)
                        }
                    })
                    presenter.getMessageSuccess(msg)
                }
            })
    }

    fun getUserDetail(userData: MutableList<User>, uid: String, presenter: UserDetailPresenter) {
        database.child("user").orderByChild("userId").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userData.clear()
                    dataSnapshot.children.mapNotNullTo(userData) {
                        it.getValue<User>(User::class.java)
                    }

                    presenter.getUserSuccess(userData)
                }
            })
    }

    fun getUserDetailEvent(events: MutableList<Event>, uid: String, presenter: UserDetailPresenter) {
        database.child("event").orderByChild("postSender").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    events.clear()
                    dataSnapshot.children.mapNotNullTo(events) {
                        it.getValue<Event>(Event::class.java)
                    }
                    reverse(events)
                    presenter.getUserEventSuccess(events)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun joinEvent(presenter: EventDetailPresenter?, eventId: String?, postSender: String?) {
        val joinId = database.child("join").push().key
        database.child("join").child(eventId!!).child(joinId!!).setValue(
            Join(
                joinId, eventId, postSender, auth.currentUser!!.uid, "2",
                System.currentTimeMillis().toString(),
                System.currentTimeMillis().toString()
            )
        ).addOnSuccessListener {
            presenter?.joinSuccess()
        }
    }

    fun cancelRequest(eventId: String, joinId: String) {
        database.child("join").child(eventId).child(joinId).removeValue()
    }

    fun cancelJoin(eventId: String, joinId: String) {
        database.child("join").child(eventId).child(joinId).removeValue()
        database.child("event").child(eventId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val slotFill: Int? = p0.child("slotFill").getValue(Int::class.java)
                val newSlot = slotFill!! - 1
                database.child("event").child(eventId).child("slotFill").setValue(newSlot)
            }
        })
    }

    fun checkJoin(presenter: EventDetailPresenter, eventId: String?) {
        database.child("join")
            .child(eventId!!)
            .orderByChild("userReqId")
            .equalTo(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val join: MutableList<Join> = mutableListOf()
                        p0.children.mapNotNullTo(join) {
                            val data = it.getValue(Join::class.java)
                            when (data?.status) {
                                "1" -> presenter.isJoin(data.joinId!!)
                                "2" -> presenter.isRequested(data.joinId!!)
                                else -> presenter.defaultJoin()
                            }
                            data
                        }
                    }
                }
            })
    }

    fun checkAccType(slotType: String, presenter: EventDetailPresenter) {
        database.child("user")
            .child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val accType = p0.child("accountType").getValue(String::class.java)
                    when (accType) {
                        "0" -> {
                            if (slotType == "1") {
                                presenter.disableJoin(slotType)
                            }
                        }
                        "1" -> {
                            if (slotType == "0") {
                                presenter.disableJoin(slotType)
                            }
                        }
                    }
                }
            })
    }

    fun getConfirmNotif(presenter: NotificationPresenter, join: MutableList<Join>) {
        database.child("join")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    join.clear()
                    for (dt: DataSnapshot in p0.children) {
                        for (dt2: DataSnapshot in dt.children) {
                            val d = dt2.getValue(Join::class.java)
                            if ((d?.postSender != auth.currentUser!!.uid) and
                                (d?.userReqId == auth.currentUser!!.uid) and
                                (d?.status != "2")
                            ) {
                                join.add(d!!)
                            }
                        }
                    }

                    Collections.sort(join, object : Comparator<Join> {
                        override fun compare(o1: Join?, o2: Join?): Int {
                            return o2?.confirmTS!!.compareTo(o1?.confirmTS!!)
                        }

                    })

                    presenter.getNotifSuccess(join)
                }
            })
    }

    fun getRequestNotif(presenter: NotificationPresenter, join: MutableList<Join>) {
        database.child("join")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    join.clear()
                    for (dt: DataSnapshot in p0.children) {
                        for (dt2: DataSnapshot in dt.children) {
                            val d = dt2.getValue(Join::class.java)
                            if ((d?.postSender == auth.currentUser!!.uid) and
                                (d?.userReqId != auth.currentUser!!.uid)
                            ) {
                                join.add(d!!)
                            }
                        }
                    }

                    Collections.sort(join, object : Comparator<Join> {
                        override fun compare(o1: Join?, o2: Join?): Int {
                            return o2?.timeStamp!!.compareTo(o1?.timeStamp!!)
                        }
                    })

                    presenter.getNotifSuccess(join)
                }
            })
    }

    fun getNotifDetail(tv: TextView, eventId: String, userId: String, joinId: String, ctx: Context) {
        var sport: String?
        var name: String?
        var confirm: String? = null
        database.child("event").child(eventId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                sport = p0.child("sportName").getValue(String::class.java)
                sport = sportPref(ctx, sport)
                database.child("user").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        name = p0.child("fullName").getValue(String::class.java)
                        database.child("join").child(eventId).child(joinId)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val status = p0.child("status").getValue(String::class.java)
                                    val usreq = p0.child("userReqId").getValue(String::class.java)
                                    when (status) {
                                        "0" -> confirm = ctx.getString(R.string.ignored)
                                        "1" -> confirm = ctx.getString(R.string.accepted)
                                    }

                                    val req = String.format(ctx.getString(R.string.request_notif), name, sport)
                                    val con = String.format(ctx.getString(R.string.confirm_notif), name, confirm)

                                    if ((status != "2") and (usreq == auth.currentUser!!.uid)) {
                                        tv.text = setBoldSpannable(con, name!!)
                                    } else {
                                        tv.text = setBoldSpannable(req, name!!)
                                    }
                                }
                            })
                    }
                })
            }
        })
    }

    fun acceptRequest(eventId: String, joinId: String) {
        database.child("join").child(eventId).child(joinId).child("status").setValue("1")
        database.child("join").child(eventId).child(joinId).child("confirmTS")
            .setValue(System.currentTimeMillis().toString())
        database.child("event").child(eventId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val slotFill: Int? = p0.child("slotFill").getValue(Int::class.java)
                val newSlot = slotFill!! + 1
                database.child("event").child(eventId).child("slotFill").setValue(newSlot)
            }
        })
    }

    fun ignoreRequest(eventId: String, joinId: String) {
        database.child("join").child(eventId).child(joinId).child("status").setValue("0")
        database.child("join").child(eventId).child(joinId).child("confirmTS")
            .setValue(System.currentTimeMillis().toString())
    }

    fun deletePost(eventId: String) {
        database.child("event").child(eventId).removeValue()
        database.child("join").child(eventId).removeValue()
    }

    fun deleteChat(userId: String) {
        database.child("message").child(auth.currentUser?.uid!!).child(userId).removeValue()
    }

    fun getSuggestedUser(presenter: ExplorePresenter, user: MutableList<User>) {
        database.child("user").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                user.clear()
                for (data: DataSnapshot in p0.children) {
                    val us = data.getValue(User::class.java)
                    if (us?.userId != auth.currentUser?.uid && user.size < 9) {
                        user.add(us!!)
                    }
                }

                presenter.getUsersSuccess(user)
            }

        })
    }

    fun editProfile(user: User, presenter: EditProfilePresenter) {
        auth.currentUser?.updateEmail(user.email!!)?.addOnSuccessListener {
            auth.currentUser?.updatePassword(user.password!!)?.addOnSuccessListener {
                database.child("user").child(user.userId!!).setValue(user)
                    .addOnSuccessListener {
                        presenter.updateSuccess()
                    }
                    .addOnFailureListener {
                        presenter.updateFailed()
                    }
            }

        }?.addOnFailureListener {
            presenter.emailUsed()
        }
    }

    fun getDataFilter(presenter: HomePresenter) {
        database.child("user").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val sport = p0.child("sportPreferred").getValue(String::class.java)
                    val city = p0.child("city").getValue(String::class.java)

                    presenter.selectionData(sport!!, city!!)
                }
            })
    }

    fun searchUser(presenter: SearchUserPresenter, user: MutableList<User?>, name: String) {
        database.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                user.clear()
                for (data: DataSnapshot in p0.children) {
                    val userData = data.getValue(User::class.java)
                    try {
                        if ((userData?.userId != auth.currentUser!!.uid) and (userData?.fullName?.contains(name, true)!!)) {
                            user.add(userData)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                presenter.getDataSuccess(user)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun editEvent(presenter: EditEventPresenter, eventId: String, event: Event) {
        database.child("event").child(eventId).setValue(event)
            .addOnSuccessListener { presenter.updateSuccess() }
            .addOnFailureListener { presenter.updateFailed() }
    }

    fun getJoinedUser(eventId: String, user: MutableList<User?>, presenter: EventDetailPresenter) {
        database.child("join").child(eventId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                user.clear()
                for (data: DataSnapshot in p0.children) {
                    val join = data.getValue(Join::class.java)
                    if (join?.status == "1") {
                        database.child("user").child(join.userReqId!!).addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(p0: DataSnapshot) {
                                val joiner = p0.getValue(User::class.java)
                                user.add(joiner)
                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}