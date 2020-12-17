/*
 * Created by Muhammad Utsman on 14/12/20 7:52 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.listing.databinding.ItemDividerBinding

class DownloadDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemDividerBinding.bind(view)

    fun bind(name: String) = binding.run {
        txtTitleDivider.text = name
    }
}