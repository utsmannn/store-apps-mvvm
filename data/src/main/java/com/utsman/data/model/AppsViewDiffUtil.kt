package com.utsman.data.model

import androidx.recyclerview.widget.DiffUtil
import com.utsman.data.model.dto.AppsView

class AppsViewDiffUtil : DiffUtil.ItemCallback<AppsView>() {
    override fun areItemsTheSame(oldItem: AppsView, newItem: AppsView): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppsView, newItem: AppsView): Boolean {
        return oldItem == newItem
    }

}