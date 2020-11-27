package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.bytesToString
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.listing.databinding.ItemListSearchBinding

class SearchAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListSearchBinding.bind(view)

    fun bind(item: AppsSealedView.AppsView, click: (AppsSealedView.AppsView) -> Unit) = binding.run {
        txtTitle.text = item.name
        txtSize.text = item.size.bytesToString()
        imgItem.loadUrl(item.icon, item.id.toString())

        val versionString = "Ver. ${item.appVersion.name}"
        txtVersion.text = versionString

        root.setOnClickListener {
            click.invoke(item)
        }
    }
}