package com.kemenag.kembangdaman.Front

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kemenag.kembangdaman.Dashboard.MainActivity
import com.kemenag.kembangdaman.R
import kotlin.text.contains

class RegisterActivity: AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val btnSelesai: Button = findViewById(R.id.btnSelesai)
        webView = findViewById(R.id.homeWebView)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        btnSelesai.setOnClickListener {
            onUserRegisterIn()
        }
        setupWebView()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }

        }
        webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/registrasi.php")
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



    private fun onUserRegisterIn() {
        intentLogin()
    }

    private fun intentLogin() {
        Toast.makeText(this, "Registrasi telah Selesai, Silahkan Login.", Toast.LENGTH_SHORT).show()
        destroyWebView()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
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

}