package com.wysiwyg.temanolga.ui.editprofile

import android.content.Context
import android.net.Uri
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class EditProfilePresenter (private val view: EditProfileView) {

    fun showData() {
        view.showProfile()
    }

    fun setUserImage(ctx: Context?, filePath: Uri) {
        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        } else {
            FirebaseApi.uploadPhoto(filePath, this)
        }
    }

    fun uploadProgress(value: Double) {
        view.showProgress(value)
    }

    fun progressComplete(imgPath: String) {
        view.hideLoading()
        view.showUpdatedPhoto(imgPath)
    }

    fun changePasswordDialog() {
        view.showChangePassword()
    }

    fun saveData(ctx: Context?, user: User, newPassword: String?) {
        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        } else {
            view.showLoading()
            FirebaseApi.editProfile(user, this, newPassword)
        }
    }

    fun updateSuccess() {
        view.successUpdate()
    }

    fun emailUsed() {
        view.hideLoading()
        view.showEmailUsed()
    }

    fun updateFailed() {
        view.hideLoading()
        view.showFailUpdate()
    }
}