package com.wysiwyg.temanolga.ui.explore

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.searchuser.SearchUserActivity
import com.wysiwyg.temanolga.ui.adapter.UserAdapter
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_explore.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.startActivity

class ExploreFragment : Fragment(), ExploreView {

    private lateinit var adapter: UserAdapter
    private val presenter = ExplorePresenter(this)
    private var user: MutableList<User> = mutableListOf()

    override fun showUsers() {
        rv_people.visible()
        tvEmptyExplore.gone()

        adapter = UserAdapter(user)
        rv_people?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        rv_people.gone()
        progressExplore.visible()
    }

    override fun hideLoading() {
        rv_people.visible()
        progressExplore.gone()
    }

    override fun showEmptyUser() {
        rv_people.gone()
        tvEmptyExplore.visible()
    }

    override fun showNoConnection() {
        snackbar(rv_people, getString(R.string.network_explore)).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_explore)
        setHasOptionsMenu(true)

        presenter.getUsers(context, user)

        btnRunningTrack.toVenue(R.string.running_track, "running+track")
        btnFutsalField.toVenue(R.string.futsal_field, "lapangan+futsal")
        btnFootballField.toVenue(R.string.football_field, "lapangan+sepak+bola")
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.nav_search -> {
                startActivity<SearchUserActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
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