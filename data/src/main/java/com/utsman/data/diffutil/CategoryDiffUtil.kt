package com.utsman.data.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.utsman.data.model.dto.CategorySealedView

class CategoryDiffUtil : DiffUtil.ItemCallback<CategorySealedView>() {
    override fun areItemsTheSame(oldItem: CategorySealedView, newItem: CategorySealedView): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategorySealedView, newItem: CategorySealedView): Boolean {
        return oldItem == newItem
    }
}