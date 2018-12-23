package com.wysiwyg.temanolga.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.adapters.SearchUserAdapter
import com.wysiwyg.temanolga.models.User
import com.wysiwyg.temanolga.presenters.SearchUserPresenter
import com.wysiwyg.temanolga.views.SearchUserView
import kotlinx.android.synthetic.main.activity_search_user.*

class SearchUserActivity : AppCompatActivity(), SearchUserView {

    private var user: MutableList<User?> = mutableListOf()
    private lateinit var adapter: SearchUserAdapter
    private val presenter = SearchUserPresenter(this)

    override fun showLoading() {
        progressSearch.visibility = View.VISIBLE
        rvSearchPeople.visibility = View.GONE
    }

    override fun hideLoading() {
        progressSearch.visibility = View.GONE
        rvSearchPeople.visibility = View.VISIBLE
    }

    override fun showUser() {
        rvSearchPeople.layoutManager = LinearLayoutManager(this)
        adapter = SearchUserAdapter(user)
        rvSearchPeople.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        setSupportActionBar(toolbarSearch)

        searchUser.requestFocus()
        searchUser.isIconified = false

        showSearchUser(searchUser)
    }

    private fun showSearchUser(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                presenter.getData(user, p0!!)
                return true
            }

        })
    }
}
