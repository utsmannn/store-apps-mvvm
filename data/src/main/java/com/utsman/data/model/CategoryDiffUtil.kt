package com.utsman.data.model

import androidx.recyclerview.widget.DiffUtil
import com.utsman.data.model.dto.CategoryView

class CategoryDiffUtil : DiffUtil.ItemCallback<CategoryView>() {
    override fun areItemsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
        return oldItem.query == newItem.query
    }

    override fun areContentsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
        return oldItem == newItem
    }
}