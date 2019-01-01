package com.wysiwyg.temanolga.activities

import android.content.Intent
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import com.mapbox.mapboxsdk.Mapbox
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Event
import kotlinx.android.synthetic.main.activity_event_detail.*
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.wysiwyg.temanolga.presenters.EventDetailPresenter
import com.wysiwyg.temanolga.views.EventDetailView
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.wysiwyg.temanolga.utils.DateTimeUtils.dateTimeFormat
import com.wysiwyg.temanolga.utils.SpinnerItem
import com.wysiwyg.temanolga.utils.SpinnerItem.slotType
import com.wysiwyg.temanolga.utils.SpinnerItem.sportPref
import com.wysiwyg.temanolga.utils.gone
import com.wysiwyg.temanolga.utils.invisible
import com.wysiwyg.temanolga.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : AppCompatActivity(), EventDetailView {
    private val presenter = EventDetailPresenter(this)
    private var event: MutableList<Event> = mutableListOf()
    private lateinit var eventId: String
    private val savedInstanceState = Bundle()

    override fun showEventData() {
        try {
            FirebaseApi.getPostSender(event[0].postSender!!, tvUserEvent, null, imgUserEvent)
            tvSportEvent.text = String.format(getString(R.string.event_invitation), sportPref(this, event[0].sportName))
            tvDateEvent.text = dateTimeFormat(event[0].date, "EEEE, dd MMMM yyyy")
            tvTimeEvent.text = event[0].time
            tvPlaceEvent.text = event[0].place?.split(",")!![0]
            tvPlaceDetail.text = event[0].place
            tvDescEvent.text = event[0].description
            if (event[0].description == "") {
                tvDescEvent.text = "-"
            }

            tvSlotJoinEvent.text =
                    String.format(
                        getString(R.string.joined_event),
                        event[0].slotFill,
                        slotType(this, event[0].slotType)
                    )

            if (event[0].slot == null) {
                tvSlotEvent.text = String.format(getString(R.string.slot_type), slotType(this, event[0].slotType))
            } else {
                tvSlotEvent.text = String.format(
                    getString(R.string.slot_type_limited),
                    event[0].slot,
                    slotType(this, event[0].slotType)
                )
            }

            if (event[0].longLat == null) {
                presenter.hideMap()
            } else {
                val longLat = event[0].longLat!!.split(",")
                presenter.getMap(savedInstanceState, longLat[1], longLat[0], mapTitle(event[0].place!!))
            }

            presenter.checkPost(event[0].postSender)

            btnJoin.join(eventId, event[0].postSender)
            btnChat.toChat(event[0].postSender!!)

            if (event[0].postSender != FirebaseApi.currentUser()) {
                imgUserEvent.toProfile(event[0].postSender!!)
            }

            btnEdit.setOnClickListener { startActivity<EditEventActivity>("event" to event[0]) }
            presenter.checkJoin(eventId)
            presenter.checkAccType(event[0].slotType!!)
            presenter.isExpire(event[0].date!!+", "+event[0].time)

            val content = "${event[0].description} \n \n" +
                    "${sportPref(this, event[0].sportName)} at " +
                    "${event[0].place}, " +
                    "${event[0].date} ${event[0].time}"
            btnShare.share(content)

        } catch (ex: Exception) {
            ex.printStackTrace()
            finish()
        }
    }

    override fun showMap(savedInstanceState: Bundle?, long: String?, lat: String?, title: String?) {
        placeMap.onCreate(savedInstanceState)
        placeMap.getMapAsync { mapboxMap ->
            mapboxMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat!!.toDouble(), long!!.toDouble()), 13.0
                )
            )
            mapboxMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lat.toDouble(), long.toDouble()))
                    .title(title)
            )
        }
        onMapTouch()
    }

    override fun hideMap() {
        placeMap.gone()
    }

    override fun showJoinMsg() {
        snackbar(tvDescEvent, "Requested").show()
    }

    override fun isOwnPost() {
        btnChat.invisible()
        btnJoin.gone()
        btnEdit.visible()
        btnDelete.visible()
    }

    override fun isUserPost() {
        btnChat.visible()
        btnJoin.visible()
        btnEdit.gone()
        btnDelete.gone()
    }

    override fun disableJoin(slotType: String) {
        btnJoin.setButtonIcon(R.drawable.ic_join)
        btnJoin.text = String.format(getString(R.string.slot_only), slotType(this, slotType))
    }

    override fun showRequested(joinId: String) {
        btnJoin.gone()
        btnJoinRequest.visible()
        btnJoinAccepted.gone()

        btnJoinRequest.setOnClickListener { presenter.cancelJoin(eventId, joinId) }
    }

    override fun showJoined(joinId: String) {
        btnJoin.gone()
        btnJoinRequest.gone()
        btnJoinAccepted.visible()

        btnJoinAccepted.setOnClickListener { presenter.cancelConfirm(joinId) }
    }

    override fun showDefJoin() {
        btnJoin.visible()
        btnJoinRequest.gone()
        btnJoinAccepted.gone()
    }

    override fun showCancelJoin(joinId: String) {
        alert("Cancel joined this invitation ?") {
            yesButton { presenter.cancelJoin(eventId, joinId) }
            noButton { it.dismiss() }
        }.show()
    }

    override fun showDeleteConfirm() {
        alert("Delete this post ?") {
            yesButton { presenter.delete(eventId) }
            noButton { it.dismiss() }
        }.show()
    }

    override fun afterDelete() {
        toast("Post Deleted").show()
    }

    override fun showExpire(date: String) {
        lytExpire.gone()

        val parseDate: Date = SimpleDateFormat("dd/MM/yyy, HH : mm", Locale.getDefault()).parse(date)
        if (Date().after(parseDate)) {
            btnJoin.setButtonIcon(R.drawable.ic_join)
            btnJoinRequest.setButtonIcon(R.drawable.ic_join_request)
            btnJoinAccepted.setButtonIcon(R.drawable.ic_join_accept)

            lytExpire.visible()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_event_detail)
        initToolbar()

        eventId = intent?.getStringExtra("eventId")!!

        btnDelete.delete()
    }

    override fun onResume() {
        super.onResume()
        presenter.getData(eventId, event)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_event)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun mapTitle(place: String): String? {
        var title = ""
        if (place.contains(",")) {
            title = event[0].place!!.split(",")[0]
        }

        return title
    }

    private fun View.join(eventId: String?, postSender: String?) {
        setOnClickListener { presenter.joinEvent(eventId, postSender) }
    }

    private fun View.toChat(sender: String) {
        setOnClickListener { startActivity<ChatRoomActivity>("userId" to sender) }
    }

    private fun View.toProfile(sender: String) {
        setOnClickListener { startActivity<UserDetailActivity>("userId" to sender) }
    }

    private fun View.delete() {
        setOnClickListener { presenter.deleteConfirm() }
    }

    private fun View.share(content: String) {
        setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Share Invitation"))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onMapTouch() {
        placeMap.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                scrollView.requestDisallowInterceptTouchEvent(true)
            } else if (event.action == MotionEvent.ACTION_UP) {
                scrollView.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }

    private fun Button.setButtonIcon(icon: Int) {
        val drawable = ContextCompat.getDrawable(context, icon)
        drawable?.setColorFilter(ContextCompat.getColor(context, R.color.colorGrey), PorterDuff.Mode.SRC_IN)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null)
        textColor = ContextCompat.getColor(context, R.color.colorGrey)
        isEnabled = false
    }
}
