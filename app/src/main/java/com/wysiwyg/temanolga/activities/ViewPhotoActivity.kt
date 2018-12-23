package com.wysiwyg.temanolga.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.squareup.picasso.Picasso
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.models.User
import kotlinx.android.synthetic.main.activity_view_photo.*

class ViewPhotoActivity : AppCompatActivity() {
    private lateinit var data: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photo)
        initToolbar()

        data = intent?.getParcelableExtra("user")!!

        tvUserImg.text = data.fullName
        Picasso.get().load(data.imgPath).resize(1000, 1000).centerCrop().into(imgUserBig)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_img)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
