package com.wysiwyg.temanolga.ui.signup

interface SignUpView {
    fun showLoading()
    fun hideLoading()
    fun showSuccessMsg()
    fun showFailMsg()
    fun finishActivity()
    fun showNoConnection()
}