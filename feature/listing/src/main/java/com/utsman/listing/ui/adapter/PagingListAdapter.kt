/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.diffutil.AppsViewDiffUtil
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.listing.R
import com.utsman.listing.ui.viewholder.GridAppViewHolder
import com.utsman.listing.ui.viewholder.SearchAppViewHolder
import com.utsman.listing.ui.viewholder.UpdatedAppViewHolder

class PagingListAdapter(
    private val holderType: HolderType = HolderType.GRID,
    private val onClick: (AppsView) -> Unit
) : PagingDataAdapter<AppsView, RecyclerView.ViewHolder>(
    AppsViewDiffUtil()
) {

    enum class HolderType {
        GRID, UPDATED, SEARCH
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            when (holderType) {
                HolderType.GRID -> (holder as GridAppViewHolder).bind(item, onClick)
                HolderType.UPDATED -> (holder as UpdatedAppViewHolder).bind(item, onClick)
                HolderType.SEARCH -> (holder as SearchAppViewHolder).bind(item, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewGrid = parent.inflate(R.layout.item_list_grid)
        val viewUpdated = parent.inflate(R.layout.item_list_updated)
        val viewSearch = parent.inflate(R.layout.item_list_search)

        return when (holderType) {
            HolderType.GRID -> GridAppViewHolder(viewGrid)
            HolderType.UPDATED -> UpdatedAppViewHolder(viewUpdated)
            HolderType.SEARCH -> SearchAppViewHolder(viewSearch)
        }
    }

}