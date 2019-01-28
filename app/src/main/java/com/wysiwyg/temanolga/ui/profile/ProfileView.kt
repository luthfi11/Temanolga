package com.wysiwyg.temanolga.ui.profile

import com.wysiwyg.temanolga.data.model.User

interface ProfileView {
    fun showLoading()
    fun hideLoading()
    fun showEventLoading()
    fun hideEventLoading()
    fun showUserData(user: List<User>)
    fun showEventData()
    fun showEmptyEvent()
    fun showLogout()
    fun doLogOut()
    fun showLogoutError()
    fun showNoConnection()
}