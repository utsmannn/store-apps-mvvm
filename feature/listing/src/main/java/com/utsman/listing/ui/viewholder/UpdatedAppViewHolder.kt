/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.toBytesReadable
import com.utsman.abstraction.extensions.detailFor
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.listing.databinding.ItemUpdatedBinding

class UpdatedAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemUpdatedBinding.bind(view)

    fun bind(item: AppsSealedView.AppsView, click: ((AppsSealedView.AppsView) -> Unit)?) = binding.run {
        txtTitle.text = item.name
        txtSize.text = item.size.toBytesReadable()
        imgItem.loadUrl(item.icon, item.id.toString())

        val versionString =
            "Current ver. ${item.appVersion.name}\nUpdated ver. ${item.appVersion.apiName}"
        txtVersion.text = versionString

        btnDownload.setOnClickListener {
            click?.invoke(item)
        }

        root detailFor item.packageName
    }
}