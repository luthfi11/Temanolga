package com.wysiwyg.temanolga.views

interface EditProfileView {
    fun showLoading()
    fun hideLoading()
    fun showProfile()
    fun successUpdate()
    fun showFailUpdate()
    fun showProgress(value: Double)
    fun showUpdatedPhoto(imgPath: String)
}