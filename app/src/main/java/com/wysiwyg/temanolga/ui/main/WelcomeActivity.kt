package com.wysiwyg.temanolga.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.wysiwyg.temanolga.R
import org.jetbrains.anko.startActivity

class WelcomeActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASHDELAY: Long = 1000

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            startActivity<MainActivity>()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable, SPLASHDELAY)

    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}
