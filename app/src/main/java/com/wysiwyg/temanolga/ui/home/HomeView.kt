package com.wysiwyg.temanolga.ui.home

interface HomeView {
    fun showLoading()
    fun hideLoading()
    fun showData()
    fun selection(sport: String, city: String)
    fun showFilterDialog()
    fun showEmptyEvent()
    fun showNoConnection()
}