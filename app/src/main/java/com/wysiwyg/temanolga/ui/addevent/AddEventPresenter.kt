package com.wysiwyg.temanolga.ui.addevent

import android.content.Context
import android.widget.Spinner
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class AddEventPresenter(private val view: AddEventView) {

    fun getUserPreferred(sport: Spinner, slot: Spinner) {
        FirebaseApi.getUserSportPreferred(sport, slot)
    }

    fun addEvent(ctx: Context?, sport: String?, place: String?, date: String?, time: String?,
                 slot: Int?, slotType: String?, desc: String?, longLat: String?) {

        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        } else {
            view.showLoading()
            FirebaseApi.addEventData(this, sport, place, date, time, slot, slotType, desc, longLat)
        }
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