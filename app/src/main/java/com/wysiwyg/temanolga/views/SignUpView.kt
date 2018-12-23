package com.wysiwyg.temanolga.views

interface SignUpView {
    fun showLoading()
    fun hideLoading()
    fun showSuccessMsg()
    fun showFailMsg()
    fun finishActivity()
}