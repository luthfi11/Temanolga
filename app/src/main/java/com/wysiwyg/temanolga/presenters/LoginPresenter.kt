package com.wysiwyg.temanolga.presenters

import com.wysiwyg.temanolga.api.FirebaseApi
import com.wysiwyg.temanolga.views.LoginView

class LoginPresenter(private val view: LoginView) {

    fun login(email: String, password: String) {
        view.showLoading()
        FirebaseApi.login(email, password, this)
    }

    fun loginSuccess() {
        view.hideLoading()
        view.startActivity()
    }

    fun loginFailed() {
        view.hideLoading()
        view.showWrong()
    }
}