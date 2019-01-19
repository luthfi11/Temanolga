package com.wysiwyg.temanolga.ui.login

interface LoginView {
    fun showLoading()
    fun hideLoading()
    fun startActivity()
    fun showWrong()
    fun showNoConnection()
}