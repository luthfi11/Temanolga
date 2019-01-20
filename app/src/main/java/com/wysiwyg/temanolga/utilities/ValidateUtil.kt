package com.wysiwyg.temanolga.utilities

import android.util.Patterns
import android.widget.EditText
import android.widget.Spinner

object ValidateUtil {
    fun etToString(editText: EditText): String {
        return editText.text.toString()
    }

    fun spnPosition(spinner: Spinner): String {
        return spinner.selectedItemPosition.toString()
    }

    fun emailValidate(etEmail: EditText): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(etToString(etEmail)).matches()
    }

    fun passwordValidate(etPass: EditText): Boolean {
        return etToString(etPass).length >= 8
    }

    fun etValidate(editText: EditText): Boolean {
        return etToString(editText).isNotEmpty()
    }

    fun setError(editText: EditText, msg: String) {
        editText.requestFocus()
        editText.error = msg
    }
}