/*
 * Created by Muhammad Utsman on 14/12/20 12:32 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.detailFor
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.utils.DownloadUtils
import com.utsman.listing.R
import com.utsman.listing.databinding.ItemListDownloadedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DownloadedProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListDownloadedBinding.bind(view)

    fun bind(downloadedApps: DownloadedApps, lifecycleOwner: LifecycleOwner, mark: ((DownloadedApps) -> Unit)? = null) = binding.run {
        val name = downloadedApps.name
        val packageName = downloadedApps.appsView?.packageName
        val icon = downloadedApps.appsView?.icon
        val downloadId = downloadedApps.downloadId
        val appStatus = downloadedApps.appStatus

        txtTitle.text = name
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

            if (statusString == null) mark?.invoke(downloadedApps)

            logi("work info is ----> $workInfo")
            txtDownloadStatus.text = statusString
            btnStopDownload.setOnClickListener {
                if (total >= 0) {
                    GlobalScope.launch {
                        DownloadUtils.cancel(this, downloadId)
                    }
                }
            }

            val colorFilterProgressBar = ContextCompat.getColor(root.context, R.color.purple_500)
            val colorFilterButtonAction = ContextCompat.getColor(root.context, R.color.purple_500)
            val drawableDownloading = ContextCompat.getDrawable(root.context, R.drawable.ic_fluent_stop_24_filled)

            val actionStringDownloading = "Cancel"

            progressHorizontalDownload.progressTintList = ColorStateList.valueOf(colorFilterProgressBar)
            imgButtonDownload.setColorFilter(colorFilterButtonAction, PorterDuff.Mode.SRC_IN)

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

            logi("app status is -> $name | ${appStatus.name}")
        })
    }
}