package com.wysiwyg.temanolga.presenters

import android.widget.Spinner
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.views.AddEventView

class AddEventPresenter(private val view: AddEventView) {

    fun getUserPreferred(sport: Spinner, slot: Spinner) {
        FirebaseApi.getUserSportPreferred(sport, slot)
    }

    fun addEvent(sport: String?, place: String?, date: String?, time: String?,
                 slot: Int?, slotType: String?, desc: String?, longLat: String?) {
        view.showLoading()
        FirebaseApi.addEventData(this, sport, place, date, time, slot, slotType, desc, longLat)
    }

    fun postSuccess() {
        view.hideLoading()
        view.showSuccess()
    }

    fun postFail() {
        view.hideLoading()
        view.showFail()
    }
}