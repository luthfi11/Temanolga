package com.wysiwyg.temanolga.presenters

import android.net.Uri
import android.widget.ImageView
import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.views.EditProfileView

class EditProfilePresenter (private val view: EditProfileView) {

    fun showData() {
        view.showProfile()
    }

    fun setUserImage(filePath: Uri) {
        FirebaseApi.uploadPhoto(filePath, this)
    }

    fun uploadProgress(value: Double) {
        view.showProgress(value)
    }

    fun progressComplete(imgPath: String) {
        view.hideLoading()
        view.showUpdatedPhoto(imgPath)
    }

    fun saveData(user: User) {
        view.showLoading()
        FirebaseApi.editProfile(user, this)
    }

    fun updateSuccess() {
        view.successUpdate()
    }

    fun updateFailed() {
        view.hideLoading()
        view.showFailUpdate()
    }
}