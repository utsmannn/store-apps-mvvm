package com.utsman.home.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.inflate
import com.utsman.data.diffutil.CategoryDiffUtil
import com.utsman.data.model.dto.CategorySealedView
import com.utsman.data.model.dto.CategoryViewType
import com.utsman.home.R
import com.utsman.home.ui.holder.CategoryBannerViewHolder
import com.utsman.home.ui.holder.CategoryViewHolder

class CategoryAdapter : PagingDataAdapter<CategorySealedView, RecyclerView.ViewHolder>(CategoryDiffUtil()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is CategorySealedView.CategoryView -> (holder as CategoryViewHolder).bind(item)
            is CategorySealedView.CategoryBannerView -> (holder as CategoryBannerViewHolder).bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBanner = parent.inflate(R.layout.item_apps_category_banner)
        val viewRegular = parent.inflate(R.layout.item_apps_category)
        return when (CategoryViewType.values()[viewType]) {
            CategoryViewType.BANNER -> CategoryBannerViewHolder(viewBanner)
            CategoryViewType.REGULAR -> CategoryViewHolder(viewRegular)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType?.ordinal ?: CategoryViewType.REGULAR.ordinal
    }
}