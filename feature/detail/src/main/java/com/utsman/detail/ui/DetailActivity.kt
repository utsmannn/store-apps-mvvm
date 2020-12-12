/*
 * Created by Muhammad Utsman on 28/11/20 5:08 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Operation
import com.utsman.abstraction.interactor.listenOn
import com.utsman.abstraction.extensions.*
import com.utsman.abstraction.listener.ResultStateListener
import com.utsman.data.di._currentDownloadHelper
import com.utsman.data.di._dataStore
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.utils.DownloadUtils
import com.utsman.detail.databinding.ActivityDetailBinding
import com.utsman.detail.viewmodel.DetailViewModel
import com.utsman.network.toJson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by viewBinding()
    private val packageApps by stringExtras("package_name")
    private val viewModel: DetailViewModel by viewModels()

    private val databaseHelper = getValueOf(_currentDownloadHelper)

    private val resultListener = object : ResultStateListener<DetailView> {
        override fun onIdle() {
            logi("idle...")
        }

        override fun onLoading() {
            logi("loading...")
        }

        override fun onSuccess(data: DetailView) {
            logi("success...")
            setupView(data)
        }

        override fun onError(throwable: Throwable) {
            loge("error --> ${throwable.localizedMessage}")
            throwable.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(data: DetailView) = binding.run {
        imgDetail.loadUrl(data.icon, data.id.toString())
        txtTitle.text = data.name
        txtVersion.text = "Version ${data.appVersion.apiName}"
        txtDeveloper.text = data.developer.name
        txtDesc.text = data.description

        val size = data.file.size.bytesToString()
        val isUpdate = data.appVersion.run {
            code != 0L && apiCode > code
        }

        val isInstalled = data.appVersion.run {
            apiCode == code
        }

        val downloadTitle = when {
            isUpdate -> {
                "Update ($size)"
            }
            isInstalled -> {
                "Open"
            }
            else -> {
                "Download ($size)"
            }
        }

        btnDownload.setOnClickListener {
            if (isInstalled) {
                // open app
            } else {
                // download
                val permissions = listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                withPermissions(permissions) { _, deniedList ->
                    if (deniedList.isEmpty()) {
                        val fileName = "${data.packageName}-${data.appVersion.apiCode}"
                        val fileDownload = FileDownload.simple {
                            this.id = data.id
                            this.name = data.name
                            this.fileName = fileName
                            this.packageName = data.packageName
                            this.url = data.file.url
                        }
                        viewModel.requestDownload(fileDownload)
                    } else {
                        toast("permission denied")
                    }
                }
            }
        }

        viewModel.observerWorkInfo(packageApps)
        viewModel.workStateResult.observe(this@DetailActivity, Observer { result ->
            when (result) {
                is WorkInfoResult.Stopped -> {
                    btnDownload.text = downloadTitle
                }
                is WorkInfoResult.Downloading -> {
                    val workInfo = result.workData
                    if (workInfo != null) {
                        val doneData = workInfo.outputData.getBoolean("done", false)
                        val dataString = workInfo.progress.getString("data")
                        val fileObserver =
                            DownloadUtils.FileSizeObserver.convertFromString(dataString)

                        if (fileObserver != null) {
                            val progress = fileObserver.sizeReadable.progress
                            val total = fileObserver.sizeReadable.total
                            val soFar = fileObserver.sizeReadable.soFar

                            val textInButton = if (fileObserver.total <= 0) {
                                "Preparing download..."
                            } else {
                                "$soFar / $total ($progress)"
                            }

                            btnDownload.text = textInButton
                        }

                        logi("done data is -> $doneData")
                    } else {
                        btnDownload.text = downloadTitle
                    }
                }
            }
        })

        lifecycleScope.launch {
            databaseHelper?.getCurrentAppsFlow()?.collect { apps ->
                logi("current apps -> $apps")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        viewModel.workerState.observe(this, Observer { state ->
            when (state) {
                is Operation.State.IN_PROGRESS -> {
                    binding.txtDeveloper.text = "in progress"
                }
                is Operation.State.FAILURE -> {
                    val throwable = state.throwable
                    binding.txtDeveloper.text = throwable.localizedMessage
                }
                is Operation.State.SUCCESS -> {
                    binding.txtDeveloper.text = "success"
                }
            }
        })

        viewModel.getDetailView(packageApps)
        viewModel.detailView.observe(this, Observer {
            it.listenOn(resultListener)
            it.bindToProgressView(binding.layoutProgress, binding.parentView) {
                viewModel.getDetailView(packageApps)
            }
        })
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