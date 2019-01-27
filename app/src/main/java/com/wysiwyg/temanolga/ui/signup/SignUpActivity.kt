package com.wysiwyg.temanolga.ui.signup

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.login.LoginActivity
import com.wysiwyg.temanolga.utilities.CityUtil
import com.wysiwyg.temanolga.utilities.ValidateUtil.emailValidate
import com.wysiwyg.temanolga.utilities.ValidateUtil.etToString
import com.wysiwyg.temanolga.utilities.ValidateUtil.etValidate
import com.wysiwyg.temanolga.utilities.ValidateUtil.setError
import com.wysiwyg.temanolga.utilities.ValidateUtil.passwordValidate
import com.wysiwyg.temanolga.utilities.ValidateUtil.spnPosition
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
        toast(getString(R.string.sign_up_success)).show()
    }

    override fun showFailMsg() {
        toast(getString(R.string.sign_up_fail)).show()
    }

    override fun finishActivity() {
        startActivity<LoginActivity>()
        finish()
    }

    override fun showNoConnection() {
        snackbar(btn_signup, getString(R.string.network_signup)).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        finishActivity()
    }

    private fun initProgressBar() {
        mProgressDialog = indeterminateProgressDialog(getString(R.string.signup)+" ...", null){
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
