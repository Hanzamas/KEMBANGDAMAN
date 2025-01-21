package com.kemenag.kembangdaman.Dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kemenag.kembangdaman.Dashboard.Fragment.HomeF
import com.kemenag.kembangdaman.Dashboard.Fragment.RiwayatF
import com.kemenag.kembangdaman.Dashboard.Fragment.ServicesF
import com.kemenag.kembangdaman.Dashboard.Fragment.UsersF
import com.kemenag.kembangdaman.Front.FrontActivity
import com.kemenag.kembangdaman.R
import com.kemenag.kembangdaman.Utils.WebViewFragment
import java.util.Stack


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val homeFragment = HomeF()
    private val servicesFragment = ServicesF()
    private val usersFragment = UsersF()
    private val riwayatFragment = RiwayatF()
    private var activeFragment: Fragment = homeFragment
    private val navigationStack = Stack<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "KEMBANGDAMAN"
        navigationStack.push(R.id.navigation_home)
        setupFragments()
        setupBottomNavigation()
    }

    private fun setupFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, riwayatFragment, "Riwayat").hide(riwayatFragment)
            .add(R.id.fragmentContainer, usersFragment, "Profil").hide(usersFragment)
            .add(R.id.fragmentContainer, servicesFragment, "Layanan").hide(servicesFragment)
            .add(R.id.fragmentContainer, homeFragment, "Home")
            .commit()
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.navigation_home -> homeFragment
                R.id.navigation_service -> servicesFragment
                R.id.navigation_users -> usersFragment
                R.id.navigation_riwayat -> riwayatFragment
                else -> null
            }

            selectedFragment?.let {
                if (navigationStack.isEmpty() || navigationStack.peek() != item.itemId) {
                    navigationStack.push(item.itemId)
                }
                showFragment(it)
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment != activeFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit()
            activeFragment = fragment
        }
    }

    override fun onBackPressed() {
        val activeFragment = supportFragmentManager.fragments.find { it.isVisible }

        if (activeFragment is WebViewFragment && activeFragment.onBackPressed()) {
            // Fragment menangani Back Press (misalnya, WebView.goBack)
            return
        } else {
            if (navigationStack.size > 1) {
                // Keluarkan item terakhir dari stack
                navigationStack.pop()

                // Ambil item berikutnya dari stack
                val previousItemId = navigationStack.peek()
                bottomNavigationView.selectedItemId = previousItemId

                // Tampilkan fragment yang sesuai
                val previousFragment = when (previousItemId) {
                    R.id.navigation_home -> homeFragment
                    R.id.navigation_service -> servicesFragment
                    R.id.navigation_users -> usersFragment
                    R.id.navigation_riwayat -> riwayatFragment
                    else -> null
                }
                previousFragment?.let { showFragment(it) }
            } else {
                // Jika hanya Home yang tersisa di stack, keluar aplikasi
                hapusWebView()
                super.onBackPressed()

            }
        }


    }

    // Inflate menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option1 -> {
                ExitIntent()
                return true
            }

            R.id.menu_option2 -> {
                aboutIntent()
                return true
            }

            R.id.menu_option3 -> {
                Toast.makeText(this, "Anda telah Logout", Toast.LENGTH_SHORT).show()
                logout()
                return true
            }
            R.id.reload -> {
                val activeFragment = supportFragmentManager.fragments.find { it.isVisible }
                if (activeFragment is WebViewFragment) {
                    activeFragment.reloadWebView()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        val activeFragment = supportFragmentManager.fragments.find { it.isVisible }
        if (activeFragment is WebViewFragment) {
            activeFragment.onResume()
        }
    }
    override fun onPause() {
        super.onPause()
        val activeFragment = supportFragmentManager.fragments.find { it.isVisible }
        if (activeFragment is WebViewFragment) {
            activeFragment.pauseWebView()
        }
    }

    private fun logout() {
        hapusCache()
        hapusFragment()
        backToAuth()
        ActivityCompat.finishAffinity(this)
        finish()
    }

    private fun hapusCache() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    private fun backToAuth() {
        val intent = Intent(this, FrontActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun aboutIntent() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun ExitIntent() {
        val fragments = supportFragmentManager.fragments
        fragments.forEach { fragment ->
            if (fragment is WebViewFragment) {
                fragment.handleExit()
            }
        }
        ActivityCompat.finishAffinity(this)
        finish()

    }

    private fun hapusFragment() {
        val fragments = supportFragmentManager.fragments
        fragments.forEach { fragment ->
            if (fragment is WebViewFragment) {
                fragment.handleLogout()
            }
        }

    }

    private fun hapusWebView() {
        val fragments = supportFragmentManager.fragments
        fragments.forEach { fragment ->
            if (fragment is WebViewFragment) {
                fragment.destroyWebView()
            }
        }

    }
}
