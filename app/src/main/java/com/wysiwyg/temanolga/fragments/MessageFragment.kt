package com.wysiwyg.temanolga.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.adapters.MessageAdapter
import com.wysiwyg.temanolga.models.Message
import com.wysiwyg.temanolga.presenters.MessagePresenter
import com.wysiwyg.temanolga.utils.gone
import com.wysiwyg.temanolga.utils.visible
import com.wysiwyg.temanolga.views.MessageView
import kotlinx.android.synthetic.main.fragment_message.*

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.getMessage(msg)
        srl_message.setOnRefreshListener { presenter.getMessage(msg) }
    }
}