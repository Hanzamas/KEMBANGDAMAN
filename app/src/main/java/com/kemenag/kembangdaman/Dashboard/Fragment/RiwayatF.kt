package com.kemenag.kembangdaman.Dashboard.Fragment

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kemenag.kembangdaman.Dashboard.Fragment.ServicesF
import com.kemenag.kembangdaman.R
import com.kemenag.kembangdaman.Utils.WebViewFragment


class RiwayatF : Fragment(), WebViewFragment {
    private lateinit var webView: WebView
    private var isLogout = false // Tambahkan variabel untuk mendeteksi logout
    private var isExit = false
    private var webViewState: Bundle? = null
    private val FILE_CHOOSER_REQUEST_CODE = 1
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)
        webView = view.findViewById(R.id.riwayatWebView)
        setupWebView()

        // Restore WebView state if available
        if (webViewState != null) {
            webView.restoreState(webViewState!!)



        } else {
            webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/surat.php")
        }
        return view
    }

    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val specificUrl = "https://kembangdaman.kemenagkabjombang.my.id/users/index.php#!"

                if (url == specificUrl) {
                    webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/surat.php")
                }
                view?.loadUrl(url ?: "")
                return true
            }

        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@RiwayatF.filePathCallback?.onReceiveValue(null)
                this@RiwayatF.filePathCallback = filePathCallback

                val intent = fileChooserParams?.createIntent()
                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE)
                } catch (e: Exception) {
                    this@RiwayatF.filePathCallback = null
                    Toast.makeText(requireContext(), "Cannot open file chooser", Toast.LENGTH_SHORT).show()
                    return false
                }
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
        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            if (mimeType == "application/pdf") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(url), "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                try {
                    startActivity(intent)
                    webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/surat.php")
                } catch (e: Exception) {
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.setMimeType(mimeType)
                    val cookies = CookieManager.getInstance().getCookie(url)
                    request.addRequestHeader("cookie", cookies)
                    request.addRequestHeader("User-Agent", userAgent)
                    request.setDescription("Downloading file...")
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
                    request.allowScanningByMediaScanner()
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType))
                    val dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(request)
                    Toast.makeText(requireContext(), "Downloading File", Toast.LENGTH_LONG).show()
                    webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/surat.php")
                }
            } else if (mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" || mimeType == "application/msword") {
                val request = DownloadManager.Request(Uri.parse(url))
                request.setMimeType(mimeType)
                val cookies = CookieManager.getInstance().getCookie(url)
                request.addRequestHeader("cookie", cookies)
                request.addRequestHeader("User-Agent", userAgent)
                request.setDescription("Downloading file...")
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType))
                val dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                Toast.makeText(requireContext(), "Downloading File", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            filePathCallback?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
            filePathCallback = null
        }
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
            webView.onPause() // Hentikan sementara untuk menghemat resource
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
    override fun onBackPressed(): Boolean {
        return if (webView.canGoBack() ) {
            webView.goBack()
            true
        } else {
            false // Biarkan Activity menangani Back Press
        }
    }

    override fun handleExit() {
        isExit = true
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

    override fun destroyWebView() {
        stopWebView()
    }

    override fun pauseWebView() {
        if (::webView.isInitialized) {
            webView.onPause()
        }
    }

    override fun reloadWebView() {
        webView.loadUrl("https://kembangdaman.kemenagkabjombang.my.id/users/surat.php")
    }
}