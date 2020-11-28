/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.bytesToString
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.listing.databinding.ItemListUpdatedBinding

class UpdatedAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListUpdatedBinding.bind(view)

    fun bind(item: AppsSealedView.AppsView, click: (AppsSealedView.AppsView) -> Unit) = binding.run {
        txtTitle.text = item.name
        txtSize.text = item.size.bytesToString()
        imgItem.loadUrl(item.icon, item.id.toString())

        val versionString =
            "Current ver. ${item.appVersion.name}\nUpdated ver. ${item.appVersion.apiName}"
        txtVersion.text = versionString

        root.setOnClickListener {
            click.invoke(item)
        }
    }
}