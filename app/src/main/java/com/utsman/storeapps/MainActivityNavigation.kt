/*
 * Created by Muhammad Utsman on 14/12/20 4:40 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.utsman.abstraction.extensions.intentTo
import com.utsman.storeapps.databinding.ActivityMainNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityNavigation : AppCompatActivity() {

    //private val binding: ActivityMainNavigationBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)
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
}