package com.wysiwyg.temanolga.views

import com.wysiwyg.temanolga.models.User

interface ProfileView {
    fun showLoading()
    fun hideLoading()
    fun showUserData(user: List<User>)
    fun showEventData()
    fun showEmptyEvent()
    fun showLogout()
    fun doLogOut()
}