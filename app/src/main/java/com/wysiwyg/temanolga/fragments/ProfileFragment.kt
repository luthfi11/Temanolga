package com.wysiwyg.temanolga.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.activities.EditProfileActivity
import com.wysiwyg.temanolga.activities.LoginActivity
import com.wysiwyg.temanolga.activities.ViewPhotoActivity
import com.wysiwyg.temanolga.adapters.EventAdapter
import com.wysiwyg.temanolga.models.Event
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.presenters.ProfilePresenter
import com.wysiwyg.temanolga.utils.SpinnerItem
import com.wysiwyg.temanolga.views.ProfileView
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.yesButton

class ProfileFragment : Fragment(), ProfileView {
    private val presenter = ProfilePresenter(this)
    private val userData: MutableList<User> = mutableListOf()
    private val events: MutableList<Event> = mutableListOf()
    private lateinit var adapter: EventAdapter

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun showUserData(user: List<User>) {
        userData.addAll(user)
        try {
            Picasso.get().load(userData[0].imgPath).resize(200, 200)
                .centerCrop().placeholder(R.color.colorMuted).into(imgUser)
            tvUserFullName.text = userData[0].fullName
            tvUserSport.text = sport(userData[0].accountType, userData[0].sportPreferred)
            tvUserCity.text = userData[0].city
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun doLogOut() {
        activity?.finish()
        startActivity<LoginActivity>()
    }

    override fun showEventData() {
        rv_event_profile?.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter(events)
        rv_event_profile?.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.getUser(userData)
        presenter.getUserEvent(events)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        imgUser.setOnClickListener { startActivity<ViewPhotoActivity>("user" to userData[0]) }
    }

    private fun sport(accType: String?, sport: String?): String {
        return when (accType) {
            "1" -> String.format(getString(R.string.acc_team), SpinnerItem.sportPref(context!!, sport))
            else -> String.format(getString(R.string.acc_personal), SpinnerItem.sportPref(context!!, sport))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.nav_edit -> startActivity<EditProfileActivity>("user" to userData[0])
            R.id.nav_logout -> logOutPrompt()
        }
        return true
    }

    private fun logOutPrompt() {
        alert("Logout from this account ?") {
            yesButton { presenter.logOut() }
            noButton { it.dismiss() }
        }.show()
    }

}