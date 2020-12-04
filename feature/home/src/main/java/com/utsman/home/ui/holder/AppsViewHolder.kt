/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.ui.holder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.bytesToString
import com.utsman.abstraction.ext.detailFor
import com.utsman.abstraction.ext.loadRes
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.home.databinding.ItemAppsBinding

class AppsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAppsBinding.bind(view)

    fun bind(item: AppsSealedView.AppsView) = binding.run {
        txtTitle.text = item.name
        txtSize.text = item.size.bytesToString()
        imgItem.loadUrl(item.icon, item.id.toString())

        imgLabel.isVisible = item.iconLabel != null
        if (item.iconLabel != null) imgLabel.loadRes(item.iconLabel!!, item.id.toString())

        root detailFor item.packageName
    }
}