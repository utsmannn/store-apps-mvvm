/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.diffutil.AppsViewDiffUtil
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.listing.R
import com.utsman.listing.ui.viewholder.DownloadedViewHolder
import com.utsman.listing.ui.viewholder.GridAppViewHolder
import com.utsman.listing.ui.viewholder.SearchAppViewHolder
import com.utsman.listing.ui.viewholder.UpdatedAppViewHolder

class PagingListAdapter(
    private var holderType: HolderType = HolderType.GRID,
    private val lifecycleOwner: LifecycleOwner
) : PagingDataAdapter<AppsView, RecyclerView.ViewHolder>(
    AppsViewDiffUtil()
) {

    private var onClick: ((AppsView) -> Unit)? = null
    private var openDownload:((String) -> Unit)? = null

    fun onUpdateClick(click: (AppsView) -> Unit) {
        this.onClick = click
    }

    fun openDownloadFile(openDownloadFile: ((String) -> Unit)) {
        this.openDownload = openDownloadFile
    }

    enum class HolderType {
        GRID, UPDATED, SEARCH
    }

    enum class HolderDownloadedType {
        REGULAR, DOWNLOADED
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            when (holderType) {
                HolderType.GRID -> (holder as GridAppViewHolder).bind(item)
                HolderType.UPDATED -> (holder as UpdatedAppViewHolder).bind(item, onClick)
                HolderType.SEARCH -> {
                    when (HolderDownloadedType.values()[getItemViewType(position)]) {
                        HolderDownloadedType.REGULAR -> (holder as SearchAppViewHolder).bind(item)
                        else -> (holder as DownloadedViewHolder).bind(item.downloadedApps!!, lifecycleOwner, openDownload)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewGrid = parent.inflate(R.layout.item_grid)
        val viewUpdated = parent.inflate(R.layout.item_updated)
        val viewSearch = parent.inflate(R.layout.item_search)

        val listDownloadedView = parent.inflate(R.layout.item_downloaded)

        return when (holderType) {
            HolderType.GRID -> GridAppViewHolder(viewGrid)
            HolderType.UPDATED -> UpdatedAppViewHolder(viewUpdated)
            HolderType.SEARCH -> {
                when (HolderDownloadedType.values()[viewType]) {
                    HolderDownloadedType.REGULAR -> SearchAppViewHolder(viewSearch)
                    else -> DownloadedViewHolder(listDownloadedView)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val itemHasDownloaded = item?.downloadedApps != null
        return if (itemHasDownloaded) {
            HolderDownloadedType.DOWNLOADED.ordinal
        } else {
            HolderDownloadedType.REGULAR.ordinal
        }
    }

}