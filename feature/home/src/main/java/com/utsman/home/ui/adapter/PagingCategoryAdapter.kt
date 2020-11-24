package com.utsman.home.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.utsman.abstraction.ext.inflate
import com.utsman.data.model.CategoryDiffUtil
import com.utsman.data.model.dto.CategoryView
import com.utsman.home.R
import com.utsman.home.ui.holder.HomeViewHolder

class PagingCategoryAdapter : PagingDataAdapter<CategoryView, HomeViewHolder>(CategoryDiffUtil()) {
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = parent.inflate(R.layout.item_apps_category)
        return HomeViewHolder(view)
    }
}