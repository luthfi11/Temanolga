package com.wysiwyg.temanolga.ui.message

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.data.model.Message
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_message.*
import org.jetbrains.anko.design.snackbar

class MessageFragment : Fragment(), MessageView {

    private lateinit var adapter: MessageAdapter
    private val presenter = MessagePresenter(this)
    private var msg: MutableList<Message> = mutableListOf()

    override fun showMessage() {
        rv_message.visible()
        tvEmptyMessage.gone()

        rv_message?.layoutManager = LinearLayoutManager(activity)
        adapter = MessageAdapter(msg)
        rv_message?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        srl_message?.isRefreshing = true
    }

    override fun hideLoading() {
        srl_message?.isRefreshing = false
    }

    override fun showEmptyMessage() {
        rv_message.gone()
        tvEmptyMessage.visible()
    }

    override fun showNoConnection() {
        snackbar(rv_message, getString(R.string.network_message)).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_message)
    }

    override fun onResume() {
        super.onResume()
        presenter.getMessage(context, msg)
        srl_message.setOnRefreshListener { presenter.getMessage(context, msg) }
    }
}