package com.wysiwyg.temanolga.ui.message

interface MessageView {
    fun showMessage()
    fun showLoading()
    fun hideLoading()
    fun showEmptyMessage()
    fun showNoConnection()
}