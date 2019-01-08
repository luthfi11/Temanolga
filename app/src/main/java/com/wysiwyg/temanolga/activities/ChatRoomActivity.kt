package com.wysiwyg.temanolga.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.adapters.ChatRoomAdapter
import com.wysiwyg.temanolga.models.Message
import com.wysiwyg.temanolga.presenters.ChatRoomPresenter
import com.wysiwyg.temanolga.utils.ValidateUtil.etToString
import com.wysiwyg.temanolga.views.ChatRoomView
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ChatRoomActivity : AppCompatActivity(), ChatRoomView {

    private lateinit var adapter: ChatRoomAdapter
    private val presenter = ChatRoomPresenter(this)
    private var msg: MutableList<Message> = mutableListOf()

    override fun showMessage() {
        adapter = ChatRoomAdapter(msg)
        rv_chat_room?.layoutManager = LinearLayoutManager(this)
        rv_chat_room?.adapter = adapter
        rv_chat_room.layoutManager?.scrollToPosition(msg.size-1)
        adapter.notifyDataSetChanged()
    }

    override fun showFail() {
        toast("Network Error")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        val user = intent.getStringExtra("userId")
        initToolbar()
        doAsync { initView(user) }
        toolbar_message.viewUser(user)
        validateText()
        btnSend.send(user)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_message)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initView(user: String) {
        presenter.getReceiver(user, tvUserMsgRoom, tvUserCityRoom, imgUserRoom)
        presenter.getMessage(user, msg)
        presenter.setRead(user)
    }

    private fun View.viewUser(user: String){
        setOnClickListener {
            startActivity<UserDetailActivity>("userId" to user)
        }
    }

    private fun validateText() {
        btnSend.setColorFilter(ContextCompat.getColor(this, R.color.colorMuted))
        btnSend.isEnabled = false
        et_message.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_message.text.isNotEmpty()) {
                    btnSend.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                    btnSend.isEnabled = true
                } else {
                    btnSend.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorMuted))
                    btnSend.isEnabled = false
                }
            }
        })
    }

    private fun View.send(user: String) {
        setOnClickListener {
            presenter.sendMessage(user, etToString(et_message))
            et_message.setText("")
            validateText()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
