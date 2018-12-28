package com.wysiwyg.temanolga.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.R.id.*
import com.wysiwyg.temanolga.presenters.AddEventPresenter
import com.wysiwyg.temanolga.utils.DateTimePicker
import com.wysiwyg.temanolga.utils.ValidateUtil.etToString
import com.wysiwyg.temanolga.utils.ValidateUtil.etValidate
import com.wysiwyg.temanolga.utils.ValidateUtil.setError
import com.wysiwyg.temanolga.utils.ValidateUtil.spnPosition
import com.wysiwyg.temanolga.views.AddEventView
import kotlinx.android.synthetic.main.activity_add_event.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class AddEventActivity : AppCompatActivity(), AddEventView {
    private val presenter = AddEventPresenter(this)
    private lateinit var mProgressDialog: ProgressDialog
    private var longLat: String? = null

    override fun showLoading() {
        mProgressDialog
    }

    override fun hideLoading() {
        mProgressDialog.dismiss()
    }

    override fun showSuccess() {
        toast("Invitation posted")
        finish()
    }

    override fun showFail() {
        toast("Network Error")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        initToolbar()
        initDialog()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_event)
        supportActionBar?.title = ""
    }

    private fun initProgressBar() {
        mProgressDialog = indeterminateProgressDialog("Posting", null){
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            nav_done -> {
                addEvent()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val place = data?.getStringExtra("place")
        et_place.setText(place)
        et_place.error = null
        longLat = data?.getStringExtra("longLat")?.replace("@", "")
    }

    private fun initDialog() {
        presenter.getUserPreferred(spn_sport_add, spn_slot_type)
        btnDatePicker.setOnClickListener { DateTimePicker.datePicker(et_date, tvDate, this) }
        btnTimePicker.setOnClickListener { DateTimePicker.timePicker(et_time, this) }
        btnPlaceSearch.setOnClickListener { startActivityForResult<PlaceSearchActivity>(1) }
    }

    private fun addEvent() {
        if (etValidate(et_place)) {
            if (etValidate(et_date)) {
                if (etValidate(et_time)) {

                    initProgressBar()
                    presenter.addEvent(
                        spnPosition(spn_sport_add),
                        etToString(et_place),
                        tvDate.text.toString(),
                        etToString(et_time),
                        etToString(et_slot).toIntOrNull(),
                        spnPosition(spn_slot_type),
                        etToString(et_desc),
                        longLat
                    )

                } else {
                    setError(et_time, getString(R.string.time_invalid))
                }
            } else {
                setError(et_date, getString(R.string.date_invalid))
            }
        } else {
            setError(et_place, getString(R.string.place_invalid))
        }
    }
}
