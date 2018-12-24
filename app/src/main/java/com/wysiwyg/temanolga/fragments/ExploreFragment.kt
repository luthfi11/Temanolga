package com.wysiwyg.temanolga.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.activities.SearchUserActivity
import com.wysiwyg.temanolga.activities.SportVenueActivity
import com.wysiwyg.temanolga.adapters.UserAdapter
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.presenters.ExplorePresenter
import com.wysiwyg.temanolga.views.ExploreView
import kotlinx.android.synthetic.main.fragment_explore.*
import org.jetbrains.anko.support.v4.startActivity

class ExploreFragment: Fragment(), ExploreView {

    private lateinit var adapter: UserAdapter
    private val presenter = ExplorePresenter(this)
    private var user: MutableList<User> = mutableListOf()

    override fun showUsers() {
        adapter = UserAdapter(user)
        rv_people?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        try {
            rv_people.visibility = View.GONE
            progressExplore.visibility = View.VISIBLE
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun hideLoading() {
        try {
            rv_people.visibility = View.VISIBLE
            progressExplore.visibility = View.GONE
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.getUsers(user)

        btnSearchPeople.toSearch()
        btnRunningTrack.toVenue(R.string.running_track, "running+track")
        btnFutsalField.toVenue(R.string.futsal_field, "lapangan+futsal")
        btnFootballField.toVenue(R.string.football_field, "lapangan+sepak+bola")
    }

    private fun View.toSearch() {
        setOnClickListener {
            startActivity<SearchUserActivity>()
        }
    }

    private fun View.toVenue(title: Int, venue: String) {
        setOnClickListener {
            startActivity<SportVenueActivity>(
                "title" to getString(title),
                "venue" to venue
            )
        }
    }
}