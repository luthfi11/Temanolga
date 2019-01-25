package com.wysiwyg.temanolga.ui.editprofile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.data.model.User
import com.wysiwyg.temanolga.utilities.CityUtil
import com.wysiwyg.temanolga.utilities.ValidateUtil.etToString
import com.wysiwyg.temanolga.utilities.ValidateUtil.etValidate
import com.wysiwyg.temanolga.utilities.ValidateUtil.passwordValidate
import com.wysiwyg.temanolga.utilities.ValidateUtil.setError
import com.wysiwyg.temanolga.utilities.ValidateUtil.spnPosition
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast

class EditProfileActivity : AppCompatActivity(), EditProfileView {
    private val presenter = EditProfilePresenter(this)
    private lateinit var user: User
    private lateinit var newUser: User
    private lateinit var filePath: Uri
    private lateinit var progress: ProgressDialog
    private var path: String? = null

    override fun showLoading() {
        progress
    }

    override fun hideLoading() {
        progress.dismiss()
    }

    override fun showProfile() {
        Picasso.get().load(user.imgPath).resize(200, 200).centerCrop()
            .placeholder(R.color.colorMuted).into(imgUserProfile)

        etFullName.setText(user.fullName)
        etCity.setText(user.city)
        etEmail.setText(user.email)
        etPassword.setText(user.password)

        spnSport.setSelection(user.sportPreferred?.toInt()!!)
        spnAccount.setSelection(user.accountType?.toInt()!!)

        path = user.imgPath
    }

    override fun successUpdate() {
        toast(getString(R.string.profile_updated)).show()
        finish()
    }

    override fun showFailUpdate() {
        toast(getString(R.string.network_error)).show()
    }

    override fun showProgress(value: Double) {
        progress.setMessage(getString(R.string.uploading) + value.toInt() + "%")
    }

    override fun showUpdatedPhoto(imgPath: String) {
        Picasso.get().load(imgPath).resize(200, 200).centerCrop()
            .placeholder(R.color.colorMuted).into(imgUserProfile)

        path = imgPath
    }

    override fun showNoConnection() {
        snackbar(etPassword, getString(R.string.network_edit_profile)).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initToolbar()
        initSpinner()

        user = intent?.getParcelableExtra("user")!!

        presenter.showData()

        btnEditPhoto.setOnClickListener { chooseFile() }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CityUtil.city)
        etCity.setAdapter(adapter)
        spnAccount.adapter =
                ArrayAdapter(this, R.layout.spinner_item_black, resources.getStringArray(R.array.account_type))
        spnSport.adapter =
                ArrayAdapter(this, R.layout.spinner_item_black, resources.getStringArray(R.array.sport_array))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_done -> {
                updateProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateProfile() {
        if (etValidate(etFullName)) {
            if (etValidate(etCity)) {
                if (passwordValidate(etPassword)) {

                    initProgress()
                    newUser = User(
                        user.userId, etToString(etFullName), user.email, etToString(etPassword),
                        spnPosition(spnAccount), spnPosition(spnSport), etToString(etCity), path
                    )
                    presenter.saveData(this, newUser)

                } else {
                    setError(etPassword, getString(R.string.password_length))
                }
            } else {
                setError(etCity, getString(R.string.city_invalid))
            }
        } else {
            setError(etFullName, getString(R.string.fullname_invalid))
        }
    }

    private fun initProgress() {
        progress = indeterminateProgressDialog("Updating", null) {
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.show()
        }
    }

    private fun chooseFile() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_photo)), 900)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            900 -> {
                filePath = data!!.data!!
                initProgress()
                presenter.setUserImage(this, filePath)
            }
        }
    }
}
