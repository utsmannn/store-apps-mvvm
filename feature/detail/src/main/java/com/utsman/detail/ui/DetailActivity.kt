/*
 * Created by Muhammad Utsman on 28/11/20 5:08 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Operation
import com.utsman.abstraction.interactor.listenOn
import com.utsman.abstraction.extensions.*
import com.utsman.abstraction.listener.ResultStateListener
import com.utsman.data.di._downloadedRepository
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.downloaded.Download
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.utils.DownloadUtils
import com.utsman.detail.R
import com.utsman.detail.databinding.ActivityDetailBinding
import com.utsman.detail.viewmodel.DetailViewModel
import com.utsman.network.toJson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by viewBinding()
    private val packageApps by stringExtras("package_name")
    private val viewModel: DetailViewModel by viewModels()

    private val downloadedRepository = getValueOf(_downloadedRepository)

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

    private var isDownloaded = false
    private var downloadId: Long? = 0L

    @SuppressLint("SetTextI18n")
    private fun setupView(data: DetailView) = binding.run {
        imgDetail.loadUrl(data.icon, data.id.toString())
        txtTitle.text = data.name
        txtVersion.text = "Version ${data.appVersion.apiName}"
        txtDeveloper.text = data.developer.name
        txtDesc.text = data.description

        val size = data.file.size.toBytesReadable()
        val isUpdate = data.appVersion.run {
            code != 0L && apiCode > code
        }

        val isInstalled = data.appVersion.run {
            apiCode == code
        }

        val fileName = "${data.packageName}-${data.appVersion.code}"
        val isDownloadedApk = viewModel.checkIsDownloaded(fileName)

        val downloadTitle = when {
            isUpdate -> {
                "Update ($size)"
            }
            isInstalled -> {
                "Open"
            }
            isDownloadedApk -> {
                "Install"
            }
            else -> {
                "Download ($size)"
            }
        }

        btnActionDownload.setOnClickListener {
            when {
                isInstalled -> {
                    // open app
                }
                isDownloadedApk -> {
                    // install apk
                    DownloadUtils.openDownloadFile(activity = this@DetailActivity, fileName = fileName)
                }
                else -> {
                    // download or update
                    if (isDownloaded) {
                        viewModel.cancelDownload(downloadId)
                    } else {
                        val permissions = listOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )

                        withPermissions(permissions) { _, deniedList ->
                            if (deniedList.isEmpty()) {
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
            }
        }

        val layerButton = btnActionDownload.background as LayerDrawable
        val clipLayer = layerButton.findDrawableByLayerId(R.id.clip_drawable) as ClipDrawable

        viewModel.observerWorkInfo(packageApps)
        viewModel.workStateResult.observe(this@DetailActivity, Observer { result ->
            when (result) {
                is WorkInfoResult.Stopped -> {
                    btnActionDownload.text = downloadTitle
                    isDownloaded = false
                }
                is WorkInfoResult.Working -> {
                    val workInfo = result.workData

                    isDownloaded = workInfo != null
                    if (workInfo != null) {

                        val doneData = workInfo.outputData.getBoolean("done", false)
                        val dataString = workInfo.progress.getString("data")
                        val fileObserver =
                            DownloadUtils.FileSizeObserver.convertFromString(dataString)

                        isDownloaded = fileObserver != null

                        val status = DownloadUtils.getStatus(workInfo)
                        btnActionDownload.text = status?.message

                        if (!doneData) {
                            logi("type status is ----> ${status?.type}")
                            when (status?.type) {
                                Download.TypeStatus.SUCCESS -> {
                                    clipLayer.setProgressAnimation(Download.MAX_LEVEL)
                                    viewModel.updateState()
                                }
                                Download.TypeStatus.DOWNLOADING -> {
                                    logi("file observer is --> $fileObserver")
                                    if (fileObserver != null) {
                                        downloadId = fileObserver.downloadId

                                        val soFarProgress = fileObserver.progress.toInt() * 100
                                        clipLayer.setProgressAnimation(soFarProgress)
                                    }
                                }
                                Download.TypeStatus.PREPARING -> {
                                    logi("preparing in activity ....")
                                }
                                Download.TypeStatus.CANCELING -> {
                                    clipLayer.setProgressAnimation(0)
                                }
                                else -> {
                                    // else
                                }
                            }
                        } else {
                            clipLayer.setProgressAnimation(Download.MAX_LEVEL)
                        }

                        logi("done data is -> $doneData")
                    } else {
                        btnActionDownload.text = downloadTitle
                        clipLayer.setProgressAnimation(0)
                    }
                }
            }
        })

        lifecycleScope.launch {
            downloadedRepository.getCurrentAppsSuspendFlow().collect { apps ->
                logi("current apps -> $apps")
            }
        }
    }

    private fun ClipDrawable.setProgressAnimation(toProgress: Int) {
        val animator = ObjectAnimator.ofInt(this, "level", this.level, toProgress).apply {
            duration = 200
            interpolator = DecelerateInterpolator()
        }

        if (!animator.isRunning) animator.start()

        logi("progress drawable is --> $toProgress")
        if (toProgress == Download.MAX_LEVEL) {
            animator.cancel()
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
                    //binding.txtDeveloper.text = "in progress"
                }
                is Operation.State.FAILURE -> {
                    val throwable = state.throwable
                    binding.txtDeveloper.text = throwable.localizedMessage
                }
                is Operation.State.SUCCESS -> {
                    //binding.txtDeveloper.text = "success"
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