/*
 * Created by Muhammad Utsman on 14/12/20 4:40 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.utsman.abstraction.extensions.intentTo
import com.utsman.abstraction.extensions.logi
import com.utsman.abstraction.extensions.toast
import com.utsman.storeapps.databinding.ActivityMainNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityNavigation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu_static, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_action -> {
                intentTo("com.utsman.listing.ui.activity.SearchAppActivity")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragmentDownload = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        //fragmentDownload.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
        /*if (fragmentDownload != null) {
            toast("not null")
        } else {
            toast("null")
        }*/
        //toast("from activity -> $requestCode")
    }
}