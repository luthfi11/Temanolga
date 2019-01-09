package com.wysiwyg.temanolga.activities

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.presenters.SignUpPresenter
import com.wysiwyg.temanolga.utils.CityUtil
import com.wysiwyg.temanolga.views.SignUpView
import com.wysiwyg.temanolga.utils.ValidateUtil.emailValidate
import com.wysiwyg.temanolga.utils.ValidateUtil.etToString
import com.wysiwyg.temanolga.utils.ValidateUtil.etValidate
import com.wysiwyg.temanolga.utils.ValidateUtil.setError
import com.wysiwyg.temanolga.utils.ValidateUtil.passwordValidate
import com.wysiwyg.temanolga.utils.ValidateUtil.spnPosition
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity(), SignUpView {

    private var presenter = SignUpPresenter(this)
    private lateinit var mProgressDialog: ProgressDialog

    override fun showLoading() {
        mProgressDialog
    }

    override fun hideLoading() {
        mProgressDialog.dismiss()
    }

    override fun showSuccessMsg() {
        toast("Sign Up Success")
    }

    override fun showFailMsg() {
        toast("Sign up failed, Email has been used !").show()
    }

    override fun finishActivity() {
        startActivity<LoginActivity>()
        finish()
    }

    override fun showNoConnection() {
        snackbar(btn_signup, "Network unavailable, can't sign up").show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initAdapter()

        tv_signin.setOnClickListener {
            finishActivity()
        }

        btn_signup.setOnClickListener {
            signUp()
        }
    }

    private fun initProgressBar() {
        mProgressDialog = indeterminateProgressDialog("Signing Up ...", null){
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.show()
        }
    }

    private fun initAdapter() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CityUtil.city)
        et_city.setAdapter(adapter)
        spn_account_type.adapter = ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.account_type))
        spn_sport.adapter = ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.sport_array))
    }

    private fun signUp() {
        if(etValidate(et_full_name)) {
            if (etValidate(et_city)) {
                if (emailValidate(et_email)) {
                    if (passwordValidate(et_password_signup)) {
                        initProgressBar()
                        presenter.signUp(
                            this,
                            etToString(et_full_name),
                            etToString(et_email),
                            etToString(et_password_signup),
                            spnPosition(spn_account_type),
                            spnPosition(spn_sport),
                            etToString(et_city)
                        )

                    } else {
                        setError(et_password_signup, getString(R.string.password_length))
                    }
                } else {
                    setError(et_email, getString(R.string.email_invalid))
                }
            } else {
                setError(et_city, getString(R.string.city_invalid))
            }
        } else {
            setError(et_full_name, getString(R.string.fullname_invalid))
        }
    }
}
