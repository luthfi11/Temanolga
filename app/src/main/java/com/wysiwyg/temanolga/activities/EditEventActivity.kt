package com.wysiwyg.temanolga.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.presenters.EditEventPresenter
import com.wysiwyg.temanolga.utils.DateTimePicker
import com.wysiwyg.temanolga.utils.ValidateUtil
import com.wysiwyg.temanolga.utils.ValidateUtil.etToString
import com.wysiwyg.temanolga.utils.ValidateUtil.spnPosition
import com.wysiwyg.temanolga.views.EditEventView
import kotlinx.android.synthetic.main.activity_add_event.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class EditEventActivity : AppCompatActivity(), EditEventView {

    private val presenter = EditEventPresenter(this)
    private lateinit var event: Event
    private lateinit var newEvent: Event
    private lateinit var mProgressDialog: ProgressDialog
    private var longLat: String? = null

    override fun showDetail() {
        spn_sport_add.setSelection(event.sportName?.toInt()!!)
        spn_slot_type.setSelection(event.slotType?.toInt()!!)
        et_place.setText(event.place)
        et_time.setText(event.time)
        et_slot.setText(event.slot?.toString())
        et_desc.setText(event.description)

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateFormatFull = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val dateFull = dateFormatFull.format(format.parse(event.date))

        et_date.setText(dateFull)
        tvDate.text = event.date

        longLat = event.longLat
    }

    override fun showLoading() {
        mProgressDialog
    }

    override fun hideLoading() {
        mProgressDialog.dismiss()
    }

    override fun showSuccessUpdate() {
        toast("Event updated")
        finish()
    }

    override fun showFailedUpdate() {
        toast("Network error")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        initToolbar()
        initDialog()

        event = intent?.getParcelableExtra("event")!!
        presenter.getDetail()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_event)
        supportActionBar?.title = ""
    }

    private fun initDialog() {
        btnDatePicker.setOnClickListener { DateTimePicker.datePicker(et_date, tvDate, this) }
        btnTimePicker.setOnClickListener { DateTimePicker.timePicker(et_time, this) }
        btnPlaceSearch.setOnClickListener { startActivityForResult<PlaceSearchActivity>(1) }
    }

    private fun initProgressBar() {
        mProgressDialog = indeterminateProgressDialog("Updating Invitation", null){
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.show()
        }
    }

    private fun editEvent() {
        if (ValidateUtil.etValidate(et_place)) {
            if (ValidateUtil.etValidate(et_date)) {
                if (ValidateUtil.etValidate(et_time)) {

                    initProgressBar()
                    newEvent = Event(
                        event.eventId,
                        event.postSender,
                        spnPosition(spn_sport_add),
                        etToString(et_place),
                        event.city,
                        tvDate.text.toString(),
                        etToString(et_time),
                        etToString(et_slot).toIntOrNull(),
                        spnPosition(spn_slot_type),
                        event.slotFill,
                        etToString(et_desc),
                        event.postTime,
                        longLat
                    )
                    presenter.updateEvent(event.eventId!!, newEvent)

                } else {
                    ValidateUtil.setError(et_time, getString(R.string.time_invalid))
                }
            } else {
                ValidateUtil.setError(et_date, getString(R.string.date_invalid))
            }
        } else {
            ValidateUtil.setError(et_place, getString(R.string.place_invalid))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_done -> {
                editEvent()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val place = data?.getStringExtra("place")
        et_place.setText(place)
        et_place.error = null
        longLat = data?.getStringExtra("longLat")?.replace("@", "")
    }
}
