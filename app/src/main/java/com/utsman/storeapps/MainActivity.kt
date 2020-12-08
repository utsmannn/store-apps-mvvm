/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import com.utsman.abstraction.base.SimplePagerAdapter
import com.utsman.abstraction.ext.intentTo
import com.utsman.home.ui.HomeFragment
import com.utsman.listing.ui.fragment.InstalledAppFragment
import com.utsman.storeapps.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeFragment = HomeFragment()
        //val installedAppFragment = InstalledAppFragment()
        val pagerAdapter = SimplePagerAdapter(supportFragmentManager).apply {
            addFragment(homeFragment)
        }

        binding.run {
            mainViewPager.adapter = pagerAdapter
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
}