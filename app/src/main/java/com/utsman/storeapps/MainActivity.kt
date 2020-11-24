package com.utsman.storeapps

import android.content.Intent
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import com.utsman.abstraction.base.SimplePagerAdapter
import com.utsman.abstraction.di.moduleOf
import com.utsman.home.di.homeViewModel
import com.utsman.home.ui.HomeFragment
import com.utsman.storeapps.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeFragment = HomeFragment()
        val pagerAdapter = SimplePagerAdapter(supportFragmentManager).apply {
            addFragment(homeFragment)
        }

        binding.run {
            mainViewPager.adapter = pagerAdapter
        }
    }
}