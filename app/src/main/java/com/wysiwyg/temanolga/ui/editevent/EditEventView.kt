package com.wysiwyg.temanolga.ui.editevent

interface EditEventView {
    fun showDetail()
    fun showLoading()
    fun hideLoading()
    fun showSuccessUpdate()
    fun showFailedUpdate()
    fun showNoConnection()
}