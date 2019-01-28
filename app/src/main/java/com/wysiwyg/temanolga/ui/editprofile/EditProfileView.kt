package com.wysiwyg.temanolga.ui.editprofile

interface EditProfileView {
    fun showLoading()
    fun hideLoading()
    fun showProfile()
    fun showChangePassword()
    fun successUpdate()
    fun showFailUpdate()
    fun showProgress(value: Double)
    fun showUpdatedPhoto(imgPath: String)
    fun showNoConnection()
}