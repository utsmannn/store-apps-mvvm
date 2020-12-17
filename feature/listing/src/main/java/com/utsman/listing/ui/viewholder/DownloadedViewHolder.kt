/*
 * Created by Muhammad Utsman on 13/12/20 2:24 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.app.Activity
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

class DownloadedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemListDownloadedBinding.bind(view)

    fun bind(downloadedApps: DownloadedApps, lifecycleOwner: LifecycleOwner, openDownloadFile: ((String) -> Unit)?) = binding.run {
        val name = downloadedApps.name
        val packageName = downloadedApps.appsView?.packageName
        val fileName = downloadedApps.fileName
        val icon = downloadedApps.appsView?.icon
        val appStatus = downloadedApps.appStatus

        txtTitle.text = name
        txtDownloadStatus.text = "Observing..."
        root detailFor packageName

        downloadedApps.workInfoLiveData.observe(lifecycleOwner, Observer { workInfo ->
            val statusString = workInfo.progress.getString("status_string")

            icon?.let {
                imgItem.loadUrl(it, downloadedApps.id)
            }

            txtDownloadStatus.text = statusString
            btnStopDownload.setOnClickListener {
                openDownloadFile?.invoke(fileName)
            }

            val colorFilterProgressBar = ContextCompat.getColor(root.context, R.color.purple_500)
            val colorFilterButtonAction = ContextCompat.getColor(root.context, R.color.purple_500)
            val drawableDownloaded = ContextCompat.getDrawable(root.context, R.drawable.ic_fluent_cloud_sync_complete_24_regular)
            val actionStringDownloaded = "Install"

            progressHorizontalDownload.progressTintList = ColorStateList.valueOf(colorFilterProgressBar)
            imgButtonDownload.setColorFilter(colorFilterButtonAction, PorterDuff.Mode.SRC_IN)

            logi("app status is -> $name | ${appStatus.name}")
            val isExist = DownloadUtils.checkAppIsDownloaded(root.context, fileName)
            if (isExist) {
                txtDownloadStatus.text = "Downloaded"
                txtDownloadAction.text = actionStringDownloaded
                imgButtonDownload.setImageDrawable(drawableDownloaded)
                progressHorizontalDownload.isVisible = false
            } else {
                txtDownloadStatus.text = "File not exist"
                btnStopDownload.alpha = 0.3f
                progressHorizontalDownload.isVisible = false
            }
        })
    }
}