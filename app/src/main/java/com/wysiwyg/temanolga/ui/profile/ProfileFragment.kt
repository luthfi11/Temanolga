package com.wysiwyg.temanolga.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.squareup.picasso.Picasso
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.editprofile.EditProfileActivity
import com.wysiwyg.temanolga.ui.login.LoginActivity
import com.wysiwyg.temanolga.ui.userdetail.ViewPhotoActivity
import com.wysiwyg.temanolga.ui.home.EventAdapter
import com.wysiwyg.temanolga.data.model.Event
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.SpinnerItem.accountType
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.design.snackbar
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
        progressBar?.visible()
    }

    override fun hideLoading() {
        progressBar?.gone()
    }

    override fun showEventLoading() {
        progressEventProfile?.visible()
    }

    override fun hideEventLoading() {
        progressEventProfile?.gone()
    }

    override fun showUserData(user: List<User>) {
        userData.addAll(user)

        try {
            Picasso.get().load(userData[0].imgPath).resize(200, 200)
                .centerCrop().placeholder(R.color.colorMuted).into(imgUser)
            tvUserFullName.text = userData[0].fullName
            tvUserSport.text = accountType(context!!, userData[0].accountType, userData[0].sportPreferred)
            tvUserCity.text = userData[0].city
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun doLogOut() {
        activity?.finish()
        startActivity<LoginActivity>()
    }

    override fun showNoConnection() {
        snackbar(root, getString(R.string.network_profile)).show()
    }

    override fun showEventData() {
        tvEmptyProfile.gone()
        rv_event_profile.visible()
        rv_event_profile?.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter(events)
        rv_event_profile?.adapter = adapter
    }

    override fun showEmptyEvent() {
        tvEmptyProfile.visible()
        rv_event_profile.gone()
    }

    override fun showLogout() {
        alert(getString(R.string.logout_prompt)) {
            yesButton { presenter.logOut() }
            noButton { it.dismiss() }
        }.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.getUser(context, userData)
        presenter.getUserEvent(events)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_profile)
        setHasOptionsMenu(true)

        imgUser.setOnClickListener { startActivity<ViewPhotoActivity>("user" to userData[0]) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nav_edit -> {
                try {
                    startActivity<EditProfileActivity>("user" to userData[0])
                } catch (ex: Exception){}
            }
            R.id.nav_about -> startActivity<AboutActivity>()
            R.id.nav_report -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:luthfialfarizi98@gmail.com")
                intent.putExtra(Intent.EXTRA_SUBJECT,"Temanolga Feedback")
                if (intent.resolveActivity(activity?.packageManager!!) != null) {
                    startActivity(intent)
                }
            }
            R.id.nav_logout -> presenter.logoutPrompt()
        }
        return true
    }

}