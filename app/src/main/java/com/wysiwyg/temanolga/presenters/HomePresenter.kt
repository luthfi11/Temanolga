package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.views.HomeView

class HomePresenter(private val view: HomeView) {

    fun showDataFilter() {
        FirebaseApi.getDataFilter(this)
    }

    fun selectionData(sport: String, city: String) {
        view.selection(sport, city)
    }

    fun getData(events: MutableList<Event>, sport: String, city: String) {
        view.showLoading()
        FirebaseApi.getEventData(events, sport, city,this)
    }

    fun getDataSuccess() {
        view.hideLoading()
        view.showData()
    }

    fun getDataFail() {
        view.hideLoading()
        view.showFail()
    }

    fun showDialog() {
        view.showFilterDialog()
    }
}