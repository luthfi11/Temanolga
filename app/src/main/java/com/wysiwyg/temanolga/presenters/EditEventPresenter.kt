package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.views.EditEventView

class EditEventPresenter(private val view: EditEventView) {

    fun getDetail() {
        view.showDetail()
    }

    fun updateEvent(eventId: String, event: Event) {
        view.showLoading()
        FirebaseApi.editEvent(this, eventId, event)
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