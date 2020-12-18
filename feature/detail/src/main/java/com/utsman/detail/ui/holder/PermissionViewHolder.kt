/*
 * Created by Muhammad Utsman on 18/12/20 7:26 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.data.model.dto.permission.PermissionData
import com.utsman.detail.databinding.ItemPermissionsBinding

class PermissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemPermissionsBinding.bind(view)

    fun bind(permissionData: PermissionData) = binding.run {
        txtName.text = permissionData.string
        txtDesc.text = permissionData.desc
    }
}