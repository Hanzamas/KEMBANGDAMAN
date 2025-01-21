package com.kemenag.kembangdaman.Front

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kemenag.kembangdaman.Dashboard.MainActivity
import com.kemenag.kembangdaman.R
import java.io.File

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkAndStoreCookies("https://kembangdaman.kemenagkabjombang.my.id/")

    }

    private fun checkAndStoreCookies(url: String) {
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(url)
        if (cookies != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this, "Selamat Datang di Aplikasi KEMBANGDAMAN", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000) // 3 seconds delay
//
0
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "Selamat Datang di Aplikasi KEMBANGDAMAN", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FrontActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)

        }
    }



}