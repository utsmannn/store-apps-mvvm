/*
 * Created by Muhammad Utsman on 28/11/20 5:08 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Operation
import com.utsman.abstraction.extensions.*
import com.utsman.abstraction.interactor.listenOn
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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

    private var isDownloading = false
    private var downloadId: Long? = 0L

    private fun setEnableButtonDrawable(enable: Boolean) {
        val drawable = getDrawableRes(R.drawable.ic_fluent_dismiss_circle_24_regular, Color.WHITE)
        if (enable) {
            binding.btnActionDownload.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )
        } else {
            binding.btnActionDownload.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                null
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView(data: DetailView) = binding.run {
        imgDetail.loadUrl(data.icon, data.id.toString())
        txtTitle.text = data.name
        txtVersion.text = "Version ${data.appVersion.apiName}"
        txtDeveloper.text = data.developer.name
        txtDesc.text = data.description

        btnActionDownload.setOnClickListener {
            when {
                viewModel.isInstalled() -> {
                    // open app
                    DownloadUtils.openApps(data.packageName)
                }
                isDownloading -> {
                    // cancel download
                    viewModel.cancelDownload(downloadId)
                }
                viewModel.isDownloadedApk() -> {
                    // install apk
                    if (!isDownloading) {
                        DownloadUtils.openDownloadFile(activity = this@DetailActivity, fileName = viewModel.getFileName())
                    } else {
                        viewModel.cancelDownload(downloadId)
                    }
                }
                else -> {
                    // download or update
                    val permissions = listOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                    withPermissions(permissions) { _, deniedList ->
                        if (deniedList.isEmpty()) {
                            val fileDownload = FileDownload.simple {
                                this.id = data.id
                                this.name = data.name
                                this.fileName = viewModel.getFileName()
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

        val layerButton = btnActionDownload.background as LayerDrawable
        val clipLayer = layerButton.findDrawableByLayerId(R.id.clip_drawable) as ClipDrawable

        toast("downloaded apk -> ${viewModel.isDownloadedApk()}")
        if (viewModel.isDownloadedApk()) {
            clipLayer.setProgressAnimation(Download.MAX_LEVEL)
        }

        viewModel.observerWorkInfo(packageApps)
        viewModel.workStateResult.observe(this@DetailActivity, Observer { result ->
            when (result) {
                is WorkInfoResult.Stopped -> {
                    btnActionDownload.text = viewModel.getDownloadButtonTitle()
                    isDownloading = false
                }
                is WorkInfoResult.Working -> {
                    val workInfo = result.workData

                    if (workInfo != null) {

                        val doneData = workInfo.outputData.getBoolean("done", false)
                        val dataString = workInfo.progress.getString("data")
                        val fileObserver =
                            DownloadUtils.FileSizeObserver.convertFromString(dataString)

                        val status = DownloadUtils.getStatus(workInfo)
                        btnActionDownload.text = status?.message ?: viewModel.getDownloadButtonTitle()

                        isDownloading = !doneData && status?.type == Download.TypeStatus.DOWNLOADING
                        logi("set status is ---> ${!doneData && status?.type != Download.TypeStatus.DOWNLOADING}")

                        logi("type status is ----> ${status?.type}")

                        when (status?.type) {
                            Download.TypeStatus.SUCCESS -> {
                                setEnableButtonDrawable(false)
                                clipLayer.setProgressAnimation(Download.MAX_LEVEL)
                                btnActionDownload.text = viewModel.getDownloadButtonTitle()
                            }
                            Download.TypeStatus.DOWNLOADING -> {
                                setEnableButtonDrawable(true)
                                logi("file observer is --> $fileObserver")
                                if (fileObserver != null) {
                                    downloadId = fileObserver.downloadId

                                    val soFarProgress = fileObserver.progress.toInt() * 100
                                    clipLayer.setProgressAnimation(soFarProgress)
                                }
                            }
                            Download.TypeStatus.PREPARING -> {
                                setEnableButtonDrawable(false)
                                logi("preparing in activity ....")
                            }
                            Download.TypeStatus.CANCELING -> {
                                setEnableButtonDrawable(false)
                                clipLayer.setProgressAnimation(0)
                            }
                            else -> {
                                setEnableButtonDrawable(false)
                            }
                        }

                        if (doneData) {
                            setEnableButtonDrawable(false)
                            clipLayer.setProgressAnimation(Download.MAX_LEVEL)
                        }

                        logi("done data is -> $doneData")
                    } else {
                        setEnableButtonDrawable(false)
                        btnActionDownload.text = viewModel.getDownloadButtonTitle()
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
            duration = 600
            interpolator = DecelerateInterpolator()
        }

        if (!animator.isRunning) animator.start()
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
                    // work instance on progress
                }
                is Operation.State.FAILURE -> {
                    val throwable = state.throwable
                    binding.txtDeveloper.text = throwable.localizedMessage
                }
                is Operation.State.SUCCESS -> {
                    // work instance success created
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.restartState()
    }

}