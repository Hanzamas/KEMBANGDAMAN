package com.kemenag.kembangdaman.Front

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.webkit.CookieManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.kemenag.kembangdaman.R
import java.io.File
import kotlin.apply
import kotlin.toString

class FrontActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val tvToSPrivacy: TextView = findViewById(R.id.tvToSPrivacy)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "KEMBANGDAMAN"

        btnLogin.setOnClickListener {
            intentLogin()
        }

        btnRegister.setOnClickListener {
            intentRegister()
        }


        val tosText = "ToS"
        val privacyPolicyText = "Privacy Policy"
        val fullText = "$tosText Dan $privacyPolicyText"
        val spannableString = SpannableString(fullText)

        val tosClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                intentToS()
            }
        }

        val privacyPolicyClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                intentPrivacyPolicy()
            }
        }

        spannableString.setSpan(tosClickableSpan, fullText.indexOf(tosText), fullText.indexOf(tosText) + tosText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(privacyPolicyClickableSpan, fullText.indexOf(privacyPolicyText), fullText.indexOf(privacyPolicyText) + privacyPolicyText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvToSPrivacy.text = spannableString
        tvToSPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun intentLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun intentRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun intentToS() {
        val intent = Intent(this, ToSActivity::class.java)
        startActivity(intent)
    }
    private fun intentPrivacyPolicy() {
        val intent = Intent(this, PrivacyPolicyActivity::class.java)
        startActivity(intent)
    }

}