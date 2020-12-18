/*
 * Created by Muhammad Utsman on 18/12/20 7:50 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.model.dto.permission.PermissionData
import com.utsman.detail.R
import com.utsman.detail.ui.holder.PermissionViewHolder

class PermissionAdapter(private val permissionData: List<PermissionData?>) : RecyclerView.Adapter<PermissionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val view = parent.inflate(R.layout.item_permissions)
        return PermissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        val item = permissionData[position]
        if (item != null) holder.bind(item)
    }

    override fun getItemCount(): Int {
        return permissionData.size
    }
}