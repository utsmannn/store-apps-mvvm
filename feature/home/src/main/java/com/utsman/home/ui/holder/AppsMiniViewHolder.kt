package com.utsman.home.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.home.databinding.ItemAppsMiniBinding

class AppsMiniViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAppsMiniBinding.bind(view)

    fun bind(item: AppsSealedView.AppsView) = binding.run {
        imgItem.loadUrl(item.icon, item.id.toString())
    }
}