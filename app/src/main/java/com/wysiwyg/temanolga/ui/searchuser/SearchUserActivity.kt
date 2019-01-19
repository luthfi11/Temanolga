package com.wysiwyg.temanolga.ui.searchuser

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.MenuItem
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.adapter.SearchUserAdapter
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_search_user.*
import org.jetbrains.anko.design.snackbar

class SearchUserActivity : AppCompatActivity(), SearchUserView {

    private var user: MutableList<User?> = mutableListOf()
    private lateinit var adapter: SearchUserAdapter
    private val presenter = SearchUserPresenter(this)

    override fun showLoading() {
        progressSearch.visible()
        rvSearchPeople.gone()
    }

    override fun hideLoading() {
        progressSearch.gone()
        rvSearchPeople.visible()
    }

    override fun showUser() {
        tvEmptySearch.gone()
        rvSearchPeople.visible()
        rvSearchPeople.layoutManager = LinearLayoutManager(this)
        adapter = SearchUserAdapter(user)
        rvSearchPeople.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showUserNotFound() {
        tvEmptySearch.visible()
        rvSearchPeople.gone()
    }

    override fun showNoConnection() {
        snackbar(searchUser, getString(R.string.network_error)).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        setSupportActionBar(toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                presenter.getData(applicationContext, user, p0!!)
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
