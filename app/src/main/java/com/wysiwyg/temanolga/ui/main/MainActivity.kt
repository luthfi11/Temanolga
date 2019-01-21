package com.wysiwyg.temanolga.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.R.id.*
import com.wysiwyg.temanolga.ui.addevent.AddEventActivity
import com.wysiwyg.temanolga.ui.explore.ExploreFragment
import com.wysiwyg.temanolga.ui.home.HomeFragment
import com.wysiwyg.temanolga.ui.login.LoginActivity
import com.wysiwyg.temanolga.ui.message.MessageFragment
import com.wysiwyg.temanolga.ui.notification.NotificationFragmentManager
import com.wysiwyg.temanolga.ui.profile.ProfileFragment
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

        isLogin()
        initFragment()
        fab.addEvent()
    }

    override fun onResume() {
        super.onResume()
        initNavigation()
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

    private fun loadFragment(fragment: Fragment) {
        if (fm.findFragmentByTag(fragment::class.java.simpleName) == null) {
            fm.beginTransaction().add(R.id.content, fragment, fragment::class.java.simpleName).commit()
        }
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fm.backStackEntryCount > 1) {

        }
    }

    private fun initNavigation() {
        var fragment: Fragment? = null
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                navigation_home -> {
                    fragment = home
                    fab.show()
                }
                navigation_explore -> {
                    fragment = explore
                    fab.hide()
                }
                navigation_message -> {
                    fragment = message
                    fab.hide()
                }
                navigation_notification -> {
                    fragment = notif
                    fab.hide()
                }
                navigation_profile -> {
                    fragment = profile
                    fab.show()
                }
            }
            loadFragment(fragment!!)
            true
        }
    }
}