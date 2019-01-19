package com.wysiwyg.temanolga.ui.notification

interface NotificationView {
    fun showLoading()
    fun hideLoading()
    fun showNotification()
    fun showEmptyNotif()
    fun showNoConnection()
}