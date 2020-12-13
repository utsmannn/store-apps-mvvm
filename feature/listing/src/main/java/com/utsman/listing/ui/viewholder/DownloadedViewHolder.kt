/*
 * Created by Muhammad Utsman on 13/12/20 2:24 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.graphics.ColorFilter
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.utils.DownloadUtils
import com.utsman.listing.databinding.ItemListDownloadedBinding
import com.utsman.listing.databinding.ItemSimpleTestBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DownloadedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemListDownloadedBinding.bind(view)

    fun bind(downloadedApps: DownloadedApps, lifecycleOwner: LifecycleOwner) = binding.run {

        downloadedApps.workInfoLiveData.observe(lifecycleOwner, Observer { workInfo ->
            val name = downloadedApps.name
            val icon = downloadedApps.appsView?.icon
            val downloadId = downloadedApps.downloadId

            val dataString = workInfo.progress.getString("data")
            val statusString = workInfo.progress.getString("status_string")

            val fileObserver =
                DownloadUtils.FileSizeObserver.convertFromString(dataString)

            val total = fileObserver?.total ?: 0L
            val progress = fileObserver?.progress ?: 0L

            val totalReadable = fileObserver?.sizeReadable?.total
            val progressReadable = fileObserver?.sizeReadable?.progress
            val soFarReadable = fileObserver?.sizeReadable?.soFar

            txtTitle.text = name
            txtDownloadStatus.text = statusString
            icon?.let {
                imgItem.loadUrl(it, downloadedApps.id)
            }

            btnStopDownload.setOnClickListener {
                if (total >= 0) {
                    GlobalScope.launch {
                        DownloadUtils.cancel(this, downloadId)
                    }
                }
            }

            if (total >= 0) {
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
                    this.progress = 0
                }
            }
        })
    }
}