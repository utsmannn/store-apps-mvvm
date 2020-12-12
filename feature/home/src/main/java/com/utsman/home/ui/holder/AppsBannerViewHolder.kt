/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.ui.holder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.abstraction.extensions.toSumReadable
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.home.databinding.ItemAppsBannerBinding

class AppsBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAppsBannerBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun bind(item: AppsSealedView.AppsBannerView) = binding.run {
        txtTitle.text = item.name
        txtDownloads.text = "${item.downloads.toSumReadable()} Downloads"
        imgBanner.loadUrl(url = item.image, id = item.id.toString())
    }
}