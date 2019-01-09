package com.wysiwyg.temanolga.views

interface EditEventView {
    fun showDetail()
    fun showLoading()
    fun hideLoading()
    fun showSuccessUpdate()
    fun showFailedUpdate()
    fun showNoConnection()
}