package com.kemenag.kembangdaman.Dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kemenag.kembangdaman.R


class TutorialActivity() : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tutorial"
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {
            super.onBackPressed()
            finish()
    }

}