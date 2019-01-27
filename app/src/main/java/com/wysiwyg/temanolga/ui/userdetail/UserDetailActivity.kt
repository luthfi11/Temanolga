package com.wysiwyg.temanolga.ui.userdetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_detail.*
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.home.EventAdapter
import com.wysiwyg.temanolga.data.model.Event
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.ui.chatroom.ChatRoomActivity
import com.wysiwyg.temanolga.utilities.SpinnerItem.accountType
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity

class UserDetailActivity : AppCompatActivity(), UserDetailView {
    private val presenter = UserDetailPresenter(this)
    private val userData: MutableList<User> = mutableListOf()
    private val events: MutableList<Event> = mutableListOf()
    private lateinit var adapter: EventAdapter
    private lateinit var uid: String

    override fun showUserData(user: List<User>) {
        rv_event_profile.visible()
        tvEmptyEvent.gone()

        userData.addAll(user)

        val imgUser = findViewById<ImageView>(R.id.imgUser)

        Picasso.get().load(userData[0].imgPath).resize(200,200)
            .centerCrop().placeholder(R.color.colorMuted).into(imgUser)
        tvUserFullName.text = userData[0].fullName
        tvUserSport.text = accountType(this, userData[0].accountType, userData[0].sportPreferred)
        tvUserCity.text = userData[0].city
    }

    override fun showEventData() {
        rv_event_profile?.layoutManager = LinearLayoutManager(this)
        adapter = EventAdapter(events)
        rv_event_profile?.adapter = adapter
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }

    override fun showEventLoading() {
        progressEventProfile.visible()
    }

    override fun hideEventLoading() {
        progressEventProfile.gone()
    }

    override fun showEmptyPost() {
        rv_event_profile.gone()
        tvEmptyEvent.visible()
    }

    override fun showNoConnection() {
        snackbar(rv_event_profile, getString(R.string.network_user_detail)).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        initToolbar()
        uid = intent?.getStringExtra("userId")!!
        getData(uid)

        imgUser.setOnClickListener { startActivity<ViewPhotoActivity>("user" to userData[0]) }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun getData(uid: String) {
        presenter.getUser(this, userData, uid)
        presenter.getUserEvent(events,uid)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_chat -> {
                startActivity<ChatRoomActivity>("userId" to uid)
                true
            }
            R.id.nav_refresh -> {
                getData(uid)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
