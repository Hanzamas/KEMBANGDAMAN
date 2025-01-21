package com.kemenag.kembangdaman.Front

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kemenag.kembangdaman.R


class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        webView = findViewById(R.id.homeWebView)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Privacy Policy"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        setupWebView()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
        webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/privacy_policy.php")
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_auth, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.auth_reload -> {
                webView.reload()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setupWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSettings.setEnableSmoothTransition(true)
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
    }
    private fun destroyWebView() {
        webView.apply {
            clearHistory()
            clearCache(false)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            destroy()
        }
    }
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
            destroyWebView()
            finish()
        }
    }
}