/*
 * Created by Muhammad Utsman on 22/12/20 9:51 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.utsman.abstraction.extensions.intentTo
import com.utsman.abstraction.extensions.logi
import com.utsman.network.toJson
import com.utsman.storeapps.R
import com.utsman.storeapps.databinding.ActivityOptionsBinding
import com.utsman.storeapps.databinding.DialogCleanFilesBinding
import com.utsman.storeapps.viewmodel.OptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class OptionsActivity : AppCompatActivity() {
    private val binding: ActivityOptionsBinding by viewBinding()
    private val viewModel: OptionsViewModel by viewModels()

    private val dialogClean by lazy {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_clean_files, null)
        val dialogCleanBinding = DialogCleanFilesBinding.bind(dialogView)
        val dialog = BottomSheetDialog(this).apply {
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
        dialog.setContentView(dialogCleanBinding.root)
        dialogCleanBinding.run {
            val count = viewModel.countFile()

            viewModel.downloadDirSize.observe(this@OptionsActivity, Observer { size ->
                txtTitle.text = "Delete $count files?"
                txtDesc.text = "You can save $size disk space"
            })

            progressCircular.isVisible = false

            viewModel.downloadDirSize.observe(this@OptionsActivity, Observer { size ->
                btnClean.isEnabled = size != "0 B"
            })

            btnClean.setOnClickListener {
                lifecycleScope.launch {
                    btnClean.run {
                        isEnabled = false
                        text = "Cleaning...."
                    }
                    progressCircular.isVisible = true
                    val deleted = viewModel.deleteFiles()
                    delay(3000)
                    progressCircular.isVisible = false

                    btnClean.run {
                        if (deleted) {
                            text = "Cleaning success"
                            dialog.dismiss()
                            text = "Clean"
                        } else {
                            text = "Failed"
                            delay(2000)
                            text = "Clean"
                            isEnabled = true
                        }
                        viewModel.getDownloadSize()
                    }
                }
            }
        }

        dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            logi("is root ----> ${viewModel.isRoot}")
            toggleAutoInstaller.isEnabled = viewModel.isRoot
            toggleAutoInstaller.text =
                if (viewModel.isRoot) "Auto installer" else "Auto installer (required root device)"
            btnErrorLogInstaller.text =
                if (viewModel.isRoot) "Error log installer" else "Error log installer (required root device)"

            if (!viewModel.isRoot) {
                toggleAutoInstaller.alpha = 0.9f
                btnErrorLogInstaller.alpha = 0.4f
            }

            btnErrorLogInstaller.setOnClickListener {
                if (viewModel.isRoot) intentTo(ErrorLogInstallerActivity::class.java)
            }

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

            viewModel.getDownloadSize()
            viewModel.downloadDirSize.observe(this@OptionsActivity, Observer { size ->
                btnCleanApk.text = "Clean unused apks (${size})"
                btnCleanApk.setOnClickListener {
                    dialogClean.show()
                }
            })
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