package com.wysiwyg.temanolga.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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
        fm.beginTransaction().add(R.id.content, profile, "5").hide(profile).commit()
        fm.beginTransaction().add(R.id.content, notif, "4").hide(notif).commit()
        fm.beginTransaction().add(R.id.content, message, "3").hide(message).commit()
        fm.beginTransaction().add(R.id.content, explore, "2").hide(explore).commit()
        fm.beginTransaction().add(R.id.content, home, "1").commit()
    }

    private fun loadFragment(fragment: Fragment, title: Int) {
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
        tv_toolbar_title.text = resources.getString(title)
    }

    private fun initNavigation() {
        navigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                when (p0.itemId) {
                    navigation_home -> {
                        loadFragment(home, R.string.title_home)
                        fab.show()
                        return true
                    }
                    navigation_explore -> {
                        loadFragment(explore, R.string.title_explore)
                        fab.hide()
                        return true
                    }
                    navigation_message -> {
                        loadFragment(message, R.string.title_message)
                        fab.hide()
                        return true
                    }
                    navigation_notification -> {
                        loadFragment(notif, R.string.title_notification)
                        fab.hide()
                        return true
                    }
                    navigation_profile -> {
                        loadFragment(profile, R.string.title_profile)
                        fab.show()
                        return true
                    }
                }

                return false
            }
        })
    }
}