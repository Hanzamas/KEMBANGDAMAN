package com.kemenag.kembangdaman.Front

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.kemenag.kembangdaman.Dashboard.MainActivity
import com.kemenag.kembangdaman.R
import kotlin.ranges.contains

class LoginActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        webView = findViewById(R.id.homeWebView)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Login"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        setupWebView()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request?.url.toString()
            val cookies = CookieManager.getInstance().getCookie(url)
            if (cookies != null && cookies.contains("iduserlogin")) {
                val loginCookie = extractCookie(cookies, "iduserlogin")
                if (loginCookie != null) {
                    onUserLoggedIn()
                } else {
                    onUserNotLoggedIn()
                }
            }
            view?.loadUrl(url)
            return true
        }

        }
        webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/login_user.php")
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


    private fun extractCookie(cookies: String, cookieName: String): String? {
        cookies.split(";").forEach { cookie ->
            val parts = cookie.split("=")
            if (parts.size == 2 && parts[0].trim() == cookieName) {
                return parts[1].trim()
            }
        }
        return null
    }

    private fun onUserLoggedIn() {
        intentMain()
    }

    private fun onUserNotLoggedIn() {

    }
    private fun intentMain() {
        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
        destroyWebView()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        ActivityCompat.finishAffinity(this)
        finish()
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