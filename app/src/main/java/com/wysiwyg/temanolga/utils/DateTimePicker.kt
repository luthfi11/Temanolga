package com.wysiwyg.temanolga.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimePicker {

    fun datePicker(editText: EditText, textView: TextView, context: Context){
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {
                _, years, monthOfYear, dayOfMonth ->

            c.timeInMillis = 0
            c.set(years, monthOfYear, dayOfMonth, 0, 0, 0)
            val chosenDate = c.time

            val dateFormatFull = DateFormat.getDateInstance(DateFormat.FULL)
            val dateFull = dateFormatFull.format(chosenDate)
            editText.setText(dateFull)
            editText.error = null

            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
            val date = dateFormat.format(chosenDate)
            textView.text = date

        }, year, month, day)

        dpd.show()
    }

    fun timePicker(editText: EditText, context: Context) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, h, m ->
            c.timeInMillis = 0
            c.set(0, 0, 0, h, m, 0)
            val chosenDate = c.time

            val sdf = SimpleDateFormat("HH : mm", Locale.getDefault())
            val time : String = sdf.format(chosenDate)
            editText.setText(time)
            editText.error = null

        },hour,minute,true)

        tpd.show()
    }
}