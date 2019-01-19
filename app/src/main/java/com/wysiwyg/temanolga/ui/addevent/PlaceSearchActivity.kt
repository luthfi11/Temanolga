package com.wysiwyg.temanolga.ui.addevent

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wysiwyg.temanolga.R
import kotlinx.android.synthetic.main.activity_place_search.*

class PlaceSearchActivity : AppCompatActivity() {

    private var menuItem: Menu? = null
    private var longLat: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_search)
        setSupportActionBar(toolbar_place_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initPlaceSearch()
    }

    private fun initPlaceSearch() {
        placeSearch.webViewClient = MyBrowser()
        placeSearch.settings.javaScriptEnabled = true
        placeSearch.settings.setGeolocationEnabled(true)
        placeSearch.webChromeClient = GeoWebClient()
        placeSearch.loadUrl("https://www.google.com/maps/@-6.997051,109.0665791,7.58z")
    }

    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            if(url?.split("/")!!.contains("place")){
                val place = url.split("/")
                longLat = place[6]
                tvPlaceSearch.text = place[5].replace("+", " ")

                menuItem?.getItem(0)?.isEnabled = true
                menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(baseContext, R.drawable.ic_done)

            } else {
                tvPlaceSearch.text = getString(R.string.search_place)

                menuItem?.getItem(0)?.isEnabled = false
                menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(baseContext, R.drawable.ic_done_disable)
            }

            super.onPageFinished(view, url)
        }
    }

    inner class GeoWebClient : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
            callback?.invoke(origin, true, false)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (placeSearch.canGoBack()) && (placeSearch.url.contains("place"))) {
            placeSearch.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun setPlace() {
        val resultIntent = Intent()
        resultIntent.putExtra("place", tvPlaceSearch.text)
        resultIntent.putExtra("longLat", longLat)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        menuItem = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_done -> {
                setPlace()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
