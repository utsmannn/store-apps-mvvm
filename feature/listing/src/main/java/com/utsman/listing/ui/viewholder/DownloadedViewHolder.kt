/*
 * Created by Muhammad Utsman on 13/12/20 2:24 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.utils.DownloadUtils
import com.utsman.listing.databinding.ItemSimpleTestBinding

class DownloadedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemSimpleTestBinding.bind(view)

    fun bind(downloadedApps: DownloadedApps, lifecycleOwner: LifecycleOwner) = binding.run {

        downloadedApps.workInfoLiveData.observe(lifecycleOwner, Observer { workInfo ->
            val name = downloadedApps.name
            val dataString = workInfo.progress.getString("data")
            val fileObserver =
                DownloadUtils.FileSizeObserver.convertFromString(dataString)

            val simpleString = "$name \n${fileObserver?.sizeReadable?.total} / ${fileObserver?.sizeReadable?.soFar} (${fileObserver?.sizeReadable?.progress}"
            txtTest.text = simpleString
        })
    }
}