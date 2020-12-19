/*
 * Created by Muhammad Utsman on 20/12/20 4:51 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.model.dto.setting.ErrorLog
import com.utsman.storeapps.R
import com.utsman.storeapps.viewholder.ErrorLogInstallerViewHolder

class ErrorLogInstallerAdapter : RecyclerView.Adapter<ErrorLogInstallerViewHolder>() {
    private val list: MutableList<ErrorLog?> = mutableListOf()

    fun addList(newList: List<ErrorLog>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition]?.name == newList[newItemPosition].name
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition] == newList[newItemPosition]
            }
        })

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorLogInstallerViewHolder {
        val view = parent.inflate(R.layout.item_error_log)
        return ErrorLogInstallerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ErrorLogInstallerViewHolder, position: Int) {
        val item = list[position]
        if (item != null) holder.binding(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}