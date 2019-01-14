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
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wysiwyg.temanolga.R
import kotlinx.android.synthetic.main.activity_sport_venue.*
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import com.wysiwyg.temanolga.utils.gone
import com.wysiwyg.temanolga.utils.visible

class SportVenueActivity : AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var venue: String
    private lateinit var title: String
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private val locationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String?) {
            frm_map.gone()
            lyt_enable.visible()
        }

        override fun onProviderEnabled(provider: String?) {
            frm_map.visible()
            lyt_enable.gone()
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
        mapsWeb.webViewClient = MyBrowser()
        mapsWeb.settings.javaScriptEnabled = true
        mapsWeb.settings.setGeolocationEnabled(true)
        mapsWeb.webChromeClient = GeoWebClient()
        mapsWeb.loadUrl("https://www.google.com/maps/search/$venue/@$latitude,$longitude,14z")

        Log.d("URL MAP", "https://www.google.com/maps/search/$venue/@$latitude,$longitude,14z")
    }

    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    inner class GeoWebClient : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
            callback?.invoke(origin, true, false)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (mapsWeb.canGoBack()) && (mapsWeb.url.contains("place"))) {
            mapsWeb.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
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
