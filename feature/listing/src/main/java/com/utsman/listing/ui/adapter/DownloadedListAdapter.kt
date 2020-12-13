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
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.listing.R
import com.utsman.listing.ui.viewholder.DownloadedViewHolder

class DownloadedListAdapter(private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<DownloadedViewHolder>() {
    private val list: MutableList<DownloadedApps?> = mutableListOf()

    fun updateList(newList: List<DownloadedApps>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition]?.id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition] == newList[newItemPosition]
            }
        })

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadedViewHolder {
        val view = parent.inflate(R.layout.item_list_downloaded)
        return DownloadedViewHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadedViewHolder, position: Int) {
        val item = list[position]
        if (item != null) holder.bind(item, lifecycleOwner)
    }

    override fun getItemCount(): Int {
        return list.count()
    }
}