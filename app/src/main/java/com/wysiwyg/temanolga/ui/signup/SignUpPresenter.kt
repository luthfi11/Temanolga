package com.wysiwyg.temanolga.ui.signup

import android.content.Context
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.utilities.ConnectionUtil

class SignUpPresenter(private val view: SignUpView) {

    fun signUp(ctx: Context?, fullName: String, email: String, password: String, accountType: String, sport: String, city: String) {
        if (!ConnectionUtil.isOnline(ctx)) {
            view.hideLoading()
            view.showNoConnection()
        } else {
            view.showLoading()
            FirebaseApi.signUp(fullName, email, password, accountType, sport, city, this)
        }
    }

    fun signUpSuccess() {
        view.hideLoading()
        view.showSuccessMsg()
        view.finishActivity()
    }

    fun signUpFailed() {
        view.hideLoading()
        view.showFailMsg()
    }

}