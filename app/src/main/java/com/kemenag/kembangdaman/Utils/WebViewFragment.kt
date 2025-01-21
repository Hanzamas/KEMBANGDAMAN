package com.kemenag.kembangdaman.Utils

interface WebViewFragment {
    fun stopWebView()
    fun handleLogout()
    fun destroyWebView()
    fun pauseWebView()
    fun onBackPressed(): Boolean
    fun reloadWebView()
    fun handleExit()
}