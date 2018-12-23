package com.wysiwyg.temanolga.preferences

import android.content.Context
import android.content.SharedPreferences

object UserPreference {

    fun getData(context : Context?, key : String) : String? {
        val sharedPreferences : SharedPreferences? = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(key,"")
    }

    fun saveData(context: Context?, key: String, value : String?) {
        val sharedPreferences : SharedPreferences? = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }
}
