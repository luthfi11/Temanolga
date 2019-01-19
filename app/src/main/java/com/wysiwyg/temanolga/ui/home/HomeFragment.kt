package com.wysiwyg.temanolga.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.ArrayAdapter
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.adapter.EventAdapter
import com.wysiwyg.temanolga.data.model.Event
import kotlinx.android.synthetic.main.fragment_home.*
import com.wysiwyg.temanolga.utilities.CityUtil
import com.wysiwyg.temanolga.utilities.SpinnerItem.sportPref
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.layout_filter.*
import kotlinx.android.synthetic.main.layout_filter.view.*
import org.jetbrains.anko.design.snackbar

class HomeFragment : Fragment(), HomeView {

    private lateinit var adapter: EventAdapter
    private val presenter = HomePresenter(this)
    private var events: MutableList<Event> = mutableListOf()
    private var sport: String = ""
    private var city: String = ""

    override fun showLoading() {
        srl_home?.isRefreshing = true
    }

    override fun hideLoading() {
        srl_home?.isRefreshing = false
    }

    override fun showData() {
        lytEmptyHome.gone()
        rv_data.visible()

        rv_data?.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter(events)
        rv_data?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showNoConnection() {
        snackbar(rv_data, getString(R.string.network_home)).show()
    }

    override fun selection(sport: String, city: String) {
        this.sport = sport
        this.city = city
        try {
            tvFilter.text = String.format(getString(R.string.showing_filter), sportPref(context!!, sport), city)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        presenter.getData(context, events, sport, city)
    }

    override fun showFilterDialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.layout_filter, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
            .setTitle("Filter")

        val mAlertDialog = mBuilder.show()

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, CityUtil.city)
        mDialogView.etCityFilter.setAdapter(adapter)

        mDialogView.spnSportFilter.setSelection(sport.toInt())
        mDialogView.etCityFilter.setText(city)

        mDialogView.btnFilterDone.setOnClickListener {
            mAlertDialog.dismiss()

            sport = mAlertDialog.spnSportFilter.selectedItemPosition.toString()
            city = mAlertDialog.etCityFilter.text.toString()

            if (city != "") {
                tvFilter.text = String.format(getString(R.string.showing_filter), sportPref(context!!, sport), city)
            } else {
                tvFilter.text = String.format(getString(R.string.showing_filter_no_city), sportPref(context!!, sport))
            }

            presenter.getData(context, events, sport, city)
        }
    }

    override fun showEmptyEvent() {
        rv_data.gone()
        lytEmptyHome.visible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_home)
        setHasOptionsMenu(true)

        presenter.showDataFilter()
        srl_home.setOnRefreshListener {
            presenter.getData(context, events, sport, city)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.nav_filter -> {
                presenter.showDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}