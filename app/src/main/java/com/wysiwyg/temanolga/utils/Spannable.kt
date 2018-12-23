package com.wysiwyg.temanolga.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan

object Spannable {
    fun setBoldSpannable(text: String, span: String): SpannableString {
        val spannableContent = SpannableString(text)
        spannableContent.setSpan(StyleSpan(Typeface.BOLD), 0, span.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        return spannableContent
    }
}