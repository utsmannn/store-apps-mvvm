/*
 * Created by Muhammad Utsman on 13/12/20 2:23 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.model.dto.downloaded.AppStatus
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.listing.R
import com.utsman.listing.ui.viewholder.DownloadDividerViewHolder
import com.utsman.listing.ui.viewholder.DownloadedInstalledViewHolder
import com.utsman.listing.ui.viewholder.DownloadedProgressViewHolder
import com.utsman.listing.ui.viewholder.DownloadedViewHolder

class DownloadedListAdapter(private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list: MutableList<DownloadedApps?> = mutableListOf()
    private var markIsDone: ((DownloadedApps) -> Unit)? = null

    fun markIsDone(mark: (DownloadedApps) -> Unit) {
        this.markIsDone = mark
    }

    fun updateList(newList: List<DownloadedApps?>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition]?.id == newList[newItemPosition]?.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition] == newList[newItemPosition]
            }
        })

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.item_list_downloaded)
        val dividerView = parent.inflate(R.layout.item_divider)
        return when (AppStatus.values()[viewType]) {
            AppStatus.RUNNING -> DownloadedProgressViewHolder(view)
            AppStatus.DOWNLOADED -> DownloadedViewHolder(view)
            AppStatus.INSTALLED -> DownloadedInstalledViewHolder(view)
            AppStatus.INSTALLED_DIVIDER, AppStatus.DOWNLOAD_DIVIDER ->
                DownloadDividerViewHolder(dividerView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (AppStatus.values()[getItemViewType(position)]) {
            AppStatus.RUNNING -> if (item != null)
                (holder as DownloadedProgressViewHolder).bind(item, lifecycleOwner, markIsDone)

            AppStatus.DOWNLOADED -> if (item != null)
                (holder as DownloadedViewHolder).bind(item, lifecycleOwner)

            AppStatus.INSTALLED -> if (item != null)
                (holder as DownloadedInstalledViewHolder).bind(item, lifecycleOwner)

            AppStatus.DOWNLOAD_DIVIDER, AppStatus.INSTALLED_DIVIDER -> if (item != null)
                (holder as DownloadDividerViewHolder).bind(item.name)
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return item?.appStatus?.ordinal ?: super.getItemViewType(position)
    }
}