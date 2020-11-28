/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.utsman.data.model.dto.list.AppsSealedView.AppsView

class AppsViewDiffUtil : DiffUtil.ItemCallback<AppsView>() {
    override fun areItemsTheSame(oldItem: AppsView, newItem: AppsView): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppsView, newItem: AppsView): Boolean {
        return oldItem == newItem
    }
}