package com.wysiwyg.temanolga.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wysiwyg.temanolga.R
import kotlinx.android.synthetic.main.activity_sport_venue.*
import android.content.Intent
import android.provider.Settings
import org.jetbrains.anko.indeterminateProgressDialog

class SportVenueActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var venue: String
    private lateinit var title: String
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private val locationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String?) {
            frm_map.visibility = View.GONE
            lyt_enable.visibility = View.VISIBLE
        }

        override fun onProviderEnabled(provider: String?) {
            frm_map.visibility = View.VISIBLE
            lyt_enable.visibility = View.GONE
            initMap(venue)
        }

        override fun onLocationChanged(location: Location?) {
            longitude = location?.longitude!!
            latitude = location.latitude
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        } else {
            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                longitude = location.longitude
                latitude = location.latitude
                initMap(venue)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, locationListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_venue)

        venue = intent.getStringExtra("venue")
        title = intent.getStringExtra("title")

        initToolbar(title)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btnEnableLocation.setOnClickListener {
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }

        getLocation()
    }

    private fun initToolbar(title: String) {
        setSupportActionBar(toolbarVenue)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvVenueTitle.text = title
    }

    private fun initMap(venue: String) {
        mapsWeb.visibility = View.GONE
        mapsWeb.webViewClient = MyBrowser()
        mapsWeb.settings.javaScriptEnabled = true
        mapsWeb.settings.setGeolocationEnabled(true)
        mapsWeb.webChromeClient = GeoWebClient()
        mapsWeb.loadUrl("https://www.google.com/maps/search/$venue/@$latitude,$longitude,14z")
    }

    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mapsWeb.visibility = View.VISIBLE
        }
    }

    inner class GeoWebClient : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
            callback?.invoke(origin, true, false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
