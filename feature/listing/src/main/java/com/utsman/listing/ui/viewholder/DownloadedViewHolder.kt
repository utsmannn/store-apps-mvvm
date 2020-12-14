/*
 * Created by Muhammad Utsman on 13/12/20 2:24 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.work.hasKeyWithValueOfType
import com.utsman.abstraction.extensions.detailFor
import com.utsman.abstraction.extensions.intentTo
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.downloaded.AppStatus
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.utils.DownloadUtils
import com.utsman.listing.R
import com.utsman.listing.databinding.ItemListDownloadedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DownloadedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemListDownloadedBinding.bind(view)

    fun bind(downloadedApps: DownloadedApps, lifecycleOwner: LifecycleOwner) = binding.run {
        val name = downloadedApps.name
        val packageName = downloadedApps.appsView?.packageName
        val fileName = downloadedApps.fileName
        val icon = downloadedApps.appsView?.icon
        val downloadId = downloadedApps.downloadId
        val appStatus = downloadedApps.appStatus

        txtTitle.text = name
        txtDownloadStatus.text = "Observing..."
        root detailFor packageName

        downloadedApps.workInfoLiveData.observe(lifecycleOwner, Observer { workInfo ->

            val dataString = workInfo.progress.getString("data")
            val statusString = workInfo.progress.getString("status_string")

            val fileObserver =
                DownloadUtils.FileSizeObserver.convertFromString(dataString)

            val total = fileObserver?.total ?: 0L
            val progress = fileObserver?.progress ?: 0L

            icon?.let {
                imgItem.loadUrl(it, downloadedApps.id)
            }

            txtDownloadStatus.text = statusString
            btnStopDownload.setOnClickListener {
                when (appStatus) {
                    AppStatus.RUNNING -> {
                        if (total >= 0) {
                            GlobalScope.launch {
                                DownloadUtils.cancel(this, downloadId)
                            }
                        }
                    }
                    AppStatus.DOWNLOADED -> {
                        // downloaded
                        DownloadUtils.openDownloadFile(root.context, fileName)
                    }
                    AppStatus.INSTALLED -> {
                        // installed
                        openApps(root.context, packageName)
                    }
                }
            }

            val colorFilterProgressBar = ContextCompat.getColor(root.context, R.color.purple_500)
            val colorFilterButtonAction = ContextCompat.getColor(root.context, R.color.purple_500)
            val drawableDownloading = ContextCompat.getDrawable(root.context, R.drawable.ic_fluent_stop_24_filled)
            val drawableDownloaded = ContextCompat.getDrawable(root.context, R.drawable.ic_fluent_cloud_sync_complete_24_regular)
            val drawableInstalled = ContextCompat.getDrawable(root.context, R.drawable.ic_fluent_checkmark_circle_24_regular)

            val actionStringDownloading = "Cancel"
            val actionStringDownloaded = "Install"
            val actionStringInstalled = "Open"

            progressHorizontalDownload.progressTintList = ColorStateList.valueOf(colorFilterProgressBar)
            imgButtonDownload.setColorFilter(colorFilterButtonAction, PorterDuff.Mode.SRC_IN)

            logi("app status is -> $name | ${appStatus.name}")
            when (appStatus) {
                AppStatus.RUNNING -> {
                    if (workInfo.progress.hasKeyWithValueOfType<String>("data")) {
                        runningView(actionStringDownloading, drawableDownloading, progress)
                    } else {
                        downloadedView(actionStringDownloaded, drawableDownloaded, fileName)
                    }
                }
                AppStatus.DOWNLOADED -> {
                    downloadedView(actionStringDownloaded, drawableDownloaded, fileName)
                }
                AppStatus.INSTALLED -> {
                    installedView(actionStringInstalled, drawableInstalled)
                }
            }
        })
    }

    private fun ItemListDownloadedBinding.installedView(
        actionStringInstalled: String,
        drawableInstalled: Drawable?
    ) {
        txtDownloadAction.text = actionStringInstalled
        imgButtonDownload.setImageDrawable(drawableInstalled)
        progressHorizontalDownload.isVisible = false
    }

    private fun ItemListDownloadedBinding.downloadedView(
        actionStringDownloaded: String,
        drawableDownloaded: Drawable?,
        fileName: String
    ) {
        val isExist = DownloadUtils.checkAppIsDownloaded(root.context, fileName)
        if (isExist) {
            txtDownloadStatus.text = "Downloaded"
            txtDownloadAction.text = actionStringDownloaded
            imgButtonDownload.setImageDrawable(drawableDownloaded)
            progressHorizontalDownload.isVisible = false
        } else {
            txtDownloadStatus.text = "File not exist"
            btnStopDownload.alpha = 0.3f
        }
    }

    private fun ItemListDownloadedBinding.runningView(
        actionStringDownloading: String,
        drawableDownloading: Drawable?,
        progress: Long
    ) {
        txtDownloadAction.text = actionStringDownloading
        imgButtonDownload.setImageDrawable(drawableDownloading)
        btnStopDownload.isVisible = true
        if (progress >= 0) {
            btnStopDownload.alpha = 1f
            progressHorizontalDownload.run {
                this.isIndeterminate = false
                this.max = 100
                this.progress = progress.toInt()
            }
        } else {
            btnStopDownload.alpha = 0.3f
            progressHorizontalDownload.run {
                this.isIndeterminate = true
            }
        }
    }

    private fun openApps(context: Context, packageName: String?) {
        val packageManager = context.packageManager
        if (packageName != null) {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            context.startActivity(intent)
        }
    }
}