package com.wysiwyg.temanolga.ui.editevent

import android.content.Context
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.Event
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class EditEventPresenter(private val view: EditEventView) {

    fun getDetail() {
        view.showDetail()
    }

    fun updateEvent(ctx: Context?, eventId: String, event: Event) {
        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        } else {
            view.showLoading()
            FirebaseApi.editEvent(this, eventId, event)
        }
    }

    fun updateSuccess() {
        view.hideLoading()
        view.showSuccessUpdate()
    }

    fun updateFailed() {
        view.hideLoading()
        view.showFailedUpdate()
    }
}