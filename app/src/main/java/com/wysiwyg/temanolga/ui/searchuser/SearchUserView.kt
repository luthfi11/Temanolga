package com.wysiwyg.temanolga.ui.searchuser

interface SearchUserView {
    fun showLoading()
    fun hideLoading()
    fun showUser()
    fun showUserNotFound()
    fun showNoConnection()
}