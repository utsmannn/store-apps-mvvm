/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.utsman.data.model.dto.list.CategorySealedView

class CategoryDiffUtil : DiffUtil.ItemCallback<CategorySealedView>() {
    override fun areItemsTheSame(oldItem: CategorySealedView, newItem: CategorySealedView): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategorySealedView, newItem: CategorySealedView): Boolean {
        return oldItem == newItem
    }
}