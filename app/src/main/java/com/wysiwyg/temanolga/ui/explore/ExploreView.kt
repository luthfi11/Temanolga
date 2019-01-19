package com.wysiwyg.temanolga.ui.explore

interface ExploreView {
    fun showUsers()
    fun showLoading()
    fun hideLoading()
    fun showEmptyUser()
    fun showNoConnection()
}