package com.wysiwyg.temanolga.views

interface HomeView {
    fun showLoading()
    fun hideLoading()
    fun showData()
    fun showFail()
    fun selection(sport: String, city: String)
    fun showFilterDialog()
    fun showEmptyEvent()
}