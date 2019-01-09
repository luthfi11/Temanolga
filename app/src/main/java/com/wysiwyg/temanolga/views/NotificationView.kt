package com.wysiwyg.temanolga.views

interface NotificationView {
    fun showLoading()
    fun hideLoading()
    fun showNotification()
    fun showEmptyNotif()
    fun showNoConnection()
}