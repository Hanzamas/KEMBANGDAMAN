package com.kemenag.kembangdaman.Dashboard.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.kemenag.kembangdaman.R
import com.kemenag.kembangdaman.Utils.WebViewFragment

class UsersF : Fragment(), WebViewFragment {
    private lateinit var webView: WebView
    private var isLogout = false // Tambahkan variabel untuk mendeteksi logout
    private var isExit = false
    private var webViewState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        webView = view.findViewById(R.id.usersWebView)
        setupWebView()

        // Restore WebView state if available
        if (webViewState != null) {
            webView.restoreState(webViewState!!)
        } else {
            webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/profile.php")
        }
        return view
    }


    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            setEnableSmoothTransition(true)
            loadsImagesAutomatically = true
            blockNetworkImage = false
            blockNetworkLoads = false
        }

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::webView.isInitialized) {
            webViewState = Bundle()
            webView.saveState(webViewState!!)
            outState.putBundle("webViewState", webViewState)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            webViewState = savedInstanceState.getBundle("webViewState")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isLogout) {
            stopWebView() // Hentikan WebView sepenuhnya saat logout
        } else {
            if (isExit == true) {
                destroyWebView()
            } else {
                webView.onPause() // Hentikan sementara untuk menghemat resource
            }
        }
    }

    override fun stopWebView() {
        webView.apply {
            clearHistory()
            clearCache(false)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            destroy()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::webView.isInitialized) {
            webView.onResume()
        }
    }

    override fun handleLogout() {
        isLogout = true
    }
    override fun handleExit() {
        isExit = true
    }

    override fun onBackPressed(): Boolean {
        return if (webView.canGoBack()) {
            webView.goBack() // Navigasikan WebView ke halaman sebelumnya
            true
        } else {
            false // Biarkan Activity menangani Back Press
        }
    }

    override fun destroyWebView() {
        webView.apply {
            clearHistory()
            clearCache(false)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            destroy()
        }
    }

    override fun pauseWebView() {
        if (::webView.isInitialized) {
            webView.onPause()
        }
    }

    override fun reloadWebView() {
        webView.reload()
    }

}