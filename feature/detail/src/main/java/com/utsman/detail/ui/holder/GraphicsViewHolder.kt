/*
 * Created by Muhammad Utsman on 18/12/20 3:27 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.detail.R
import com.utsman.detail.databinding.ItemGraphicBinding

class GraphicsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGraphicBinding.bind(view)

    fun bind(image: String, click: (url: String, index: Int) -> Unit, index: Int) = binding.run {
        imgGraphic.loadUrl(image, image, colorInt = R.color.purple_200)
        root.setOnClickListener {
            click.invoke(image, index)
        }
    }
}