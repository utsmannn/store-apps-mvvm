/*
 * Created by Muhammad Utsman on 18/12/20 8:38 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.scottyab.rootbeer.RootBeer
import com.utsman.abstraction.extensions.logi
import com.utsman.abstraction.extensions.toast
import com.utsman.network.toJson
import com.utsman.storeapps.databinding.ActivitySettingsBinding
import com.utsman.storeapps.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by viewBinding()
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            logi("is root ----> ${viewModel.isRoot}")
            toggleAutoInstaller.isEnabled = viewModel.isRoot
            toggleAutoInstaller.text = if (viewModel.isRoot) "Auto installer" else "Auto installer (required root device)"
            if (!viewModel.isRoot) toggleAutoInstaller.alpha = 0.7f

            lifecycleScope.launchWhenResumed {
                val settingData = viewModel.valueAutoInstallerSync()
                logi("setting data is --> ${settingData.toJson()}")
                toggleAutoInstaller.isChecked = settingData.value
            }

            lifecycleScope.launchWhenResumed {
                val settingData = viewModel.valueMaturitySync()
                toggleMaturity.isChecked = settingData.value
            }

            toggleAutoInstaller.setOnCheckedChangeListener { view, _ ->
                if (!view.isPressed) {
                    return@setOnCheckedChangeListener
                }
                viewModel.toggleAutoInstaller()
            }

            toggleMaturity.setOnCheckedChangeListener { view, _ ->
                if (!view.isPressed) {
                    return@setOnCheckedChangeListener
                }
                viewModel.toggleMaturity()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}