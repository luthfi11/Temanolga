package com.wysiwyg.temanolga.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.R.id.*
import com.wysiwyg.temanolga.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        isLogin()
        initNavigation(savedInstanceState)
        fab.addEvent()
    }

    private fun View.addEvent() {
        setOnClickListener { startActivity<AddEventActivity>() }
    }

    private fun isLogin() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            finish()
            startActivity<LoginActivity>()
        }
    }

    private fun loadFragment(savedInstanceState: Bundle?, fragment: Fragment, title: Int) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, fragment, fragment::class.java.simpleName)
                .commit()
        }
        tv_toolbar_title.text = resources.getString(title)
    }

    private fun initNavigation(savedInstanceState: Bundle?) {
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                navigation_home -> {
                    loadFragment(savedInstanceState, HomeFragment(), R.string.title_home)
                    fab.show()
                }
                navigation_explore -> {
                    loadFragment(savedInstanceState, ExploreFragment(), R.string.title_explore)
                    fab.hide()
                }
                navigation_message -> {
                    loadFragment(savedInstanceState, MessageFragment(), R.string.title_message)
                    fab.hide()
                }
                navigation_notification -> {
                    loadFragment(savedInstanceState, NotificationFragmentManager(), R.string.title_notification)
                    fab.hide()
                }
                navigation_profile -> {
                    loadFragment(savedInstanceState, ProfileFragment(), R.string.title_profile)
                    fab.show()
                }
            }
            true
        }
        navigation.selectedItemId = navigation_home
    }

}