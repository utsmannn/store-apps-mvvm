/*
 * Created by Muhammad Utsman on 18/12/20 3:29 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.detail.R
import com.utsman.detail.ui.holder.GraphicsViewHolder

class GraphicsAdapter(
    private val graphics: List<String?>,
    private val click: (url: String, index: Int) -> Unit
) : RecyclerView.Adapter<GraphicsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphicsViewHolder {
        val view = parent.inflate(R.layout.item_graphic)
        return GraphicsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GraphicsViewHolder, position: Int) {
        val item = graphics[position]
        if (item != null) holder.bind(item, click, position)
    }

    override fun getItemCount(): Int {
        return graphics.size
    }
}