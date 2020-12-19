/*
 * Created by Muhammad Utsman on 18/12/20 8:38 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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

    private val rootBeer by lazy { RootBeer(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sampleDir = "/storage/emulated/0/Android/data/com.utsman.storeapps/files/Download/com.dynamicg.timerecording-75101.apk"

        binding.run {
            logi("is root ----> ${viewModel.isRoot}")
            toggleAutoInstaller.isEnabled = viewModel.isRoot
            toggleAutoInstaller.text = if (viewModel.isRoot) "Auto installer" else "Auto installer (required root device)"
            if (!viewModel.isRoot) toggleAutoInstaller.alpha = 0.7f
        }
    }
}