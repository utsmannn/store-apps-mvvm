/*
 * Created by Muhammad Utsman on 14/12/20 9:22 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.const.CategoriesApps
import com.utsman.listing.R
import com.utsman.listing.ui.viewholder.CategoriesViewHolder

class CategoriesAdapter : RecyclerView.Adapter<CategoriesViewHolder>() {
    val list = CategoriesApps.list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = parent.inflate(R.layout.item_category)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}