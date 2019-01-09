package com.wysiwyg.temanolga

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.google.firebase.database.FirebaseDatabase

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}