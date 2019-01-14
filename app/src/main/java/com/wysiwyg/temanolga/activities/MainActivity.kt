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

    private val home = HomeFragment()
    private val explore = ExploreFragment()
    private val message = MessageFragment()
    private val notif = NotificationFragmentManager()
    private val profile = ProfileFragment()
    private var active: Fragment = home
    private val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        isLogin()
        initNavigation()
        initFragment()
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

    private fun initFragment() {
        fm.beginTransaction().add(R.id.content, home, home::class.java.simpleName).commit()
    }

    private fun loadFragment(fragment: Fragment, title: Int) {
        if (fm.findFragmentByTag(fragment::class.java.simpleName) == null) {
            fm.beginTransaction().add(R.id.content, fragment, fragment::class.java.simpleName).commit()
        }

        fm.beginTransaction().hide(active).show(fragment).commit()

        active = fragment
        tv_toolbar_title.text = resources.getString(title)
    }

    private fun initNavigation() {
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                navigation_home -> {
                    loadFragment(home, R.string.title_home)
                    fab.show()
                }
                navigation_explore -> {
                    loadFragment(explore, R.string.title_explore)
                    fab.hide()
                }
                navigation_message -> {
                    loadFragment(message, R.string.title_message)
                    fab.hide()
                }
                navigation_notification -> {
                    loadFragment(notif, R.string.title_notification)
                    fab.hide()
                }
                navigation_profile -> {
                    loadFragment(profile, R.string.title_profile)
                    fab.show()
                }
            }
            true
        }
    }
}