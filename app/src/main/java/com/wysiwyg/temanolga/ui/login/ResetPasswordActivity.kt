package com.wysiwyg.temanolga.ui.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.utilities.ConnectionUtil
import com.wysiwyg.temanolga.utilities.ValidateUtil.emailValidate
import com.wysiwyg.temanolga.utilities.ValidateUtil.etToString
import com.wysiwyg.temanolga.utilities.ValidateUtil.setError
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.yesButton

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btnReset.setOnClickListener {
            if (ConnectionUtil.isOnline(this)) {
                if (emailValidate(etEmail)) {
                    progressReset.visible()
                    etEmail.isEnabled = false
                    btnReset.isEnabled = false
                    FirebaseAuth.getInstance().sendPasswordResetEmail(etToString(etEmail)).addOnSuccessListener {
                        progressReset.gone()
                        etEmail.isEnabled = true
                        btnReset.isEnabled = true
                        alert(getString(R.string.email_sent)) {
                            yesButton { finish() }
                            isCancelable = false
                        }.show()
                    }.addOnFailureListener {
                        progressReset.gone()
                        etEmail.isEnabled = true
                        btnReset.isEnabled = true
                        alert(getString(R.string.email_not_found)) {
                            yesButton { it.dismiss() }
                            isCancelable = false
                        }.show()
                    }
                } else {
                    setError(etEmail, getString(R.string.email_invalid))
                }
            } else {
                snackbar(btnReset, R.string.network_error).show()
            }
        }
    }
}
