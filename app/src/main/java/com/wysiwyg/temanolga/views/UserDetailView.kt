package com.wysiwyg.temanolga.views

import com.wysiwyg.temanolga.models.User

interface UserDetailView {
    fun showUserData(user: List<User>)
    fun showEventData()
    fun showLoading()
    fun hideLoading()
}