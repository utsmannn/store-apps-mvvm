/*
 * Created by Muhammad Utsman on 14/12/20 7:20 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.content.Context
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
import com.utsman.listing.databinding.ItemDownloadedBinding

class DownloadedInstalledViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemDownloadedBinding.bind(view)

    fun bind(downloadedApps: DownloadedApps, lifecycleOwner: LifecycleOwner) = binding.run {
        val name = downloadedApps.name
        val packageName = downloadedApps.appsView?.packageName
        val icon = downloadedApps.appsView?.icon
        val appStatus = downloadedApps.appStatus

        txtTitle.text = name
        txtDownloadStatus.text = "Observing..."
        root detailFor packageName

        downloadedApps.workInfoLiveData.observe(lifecycleOwner, Observer {
            icon?.let {
                imgItem.loadUrl(it, downloadedApps.id)
            }

            txtDownloadStatus.text = "Installed"
            btnStopDownload.setOnClickListener {
                DownloadUtils.openApps(packageName)
            }

            val colorFilterProgressBar = ContextCompat.getColor(root.context, R.color.purple_500)
            val colorFilterButtonAction = ContextCompat.getColor(root.context, R.color.purple_500)
            val drawableInstalled = ContextCompat.getDrawable(root.context, R.drawable.ic_fluent_checkmark_circle_24_regular)

            val actionStringInstalled = "Open"

            progressHorizontalDownload.progressTintList = ColorStateList.valueOf(colorFilterProgressBar)
            imgButtonDownload.setColorFilter(colorFilterButtonAction, PorterDuff.Mode.SRC_IN)

            logi("app status is -> $name | ${appStatus.name}")
            txtDownloadAction.text = actionStringInstalled
            imgButtonDownload.setImageDrawable(drawableInstalled)
            progressHorizontalDownload.isVisible = false
        })
    }
}