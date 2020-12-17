/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.toBytesReadable
import com.utsman.abstraction.extensions.detailFor
import com.utsman.abstraction.extensions.loadRes
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.data.utils.DownloadUtils
import com.utsman.listing.databinding.ItemListSearchBinding

class SearchAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListSearchBinding.bind(view)

    fun bind(item: AppsSealedView.AppsView) = binding.run {
        txtTitle.text = item.name
        txtSize.text = item.size.toBytesReadable()
        imgItem.loadUrl(item.icon, item.id.toString())

        val versionString = "Ver. ${item.appVersion.apiName}"
        txtVersion.text = versionString

        imgLabel.isVisible = item.iconLabel != null
        if (item.iconLabel != null) imgLabel.loadRes(item.iconLabel, item.id.toString())

        root detailFor item.packageName
    }
}