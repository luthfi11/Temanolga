package com.wysiwyg.temanolga.views

interface HomeView {
    fun showLoading()
    fun hideLoading()
    fun showData()
    fun selection(sport: String, city: String)
    fun showFilterDialog()
    fun showEmptyEvent()
    fun showNoConnection()
}