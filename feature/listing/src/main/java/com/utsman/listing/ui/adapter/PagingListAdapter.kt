package com.utsman.listing.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.bytesToString
import com.utsman.abstraction.ext.inflate
import com.utsman.abstraction.ext.loadRes
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.diffutil.AppsViewDiffUtil
import com.utsman.data.model.dto.AppsSealedView.AppsView
import com.utsman.listing.R
import com.utsman.listing.databinding.ItemListAppsBinding

class PagingListAdapter(private val onClick: (AppsView) -> Unit) : PagingDataAdapter<AppsView, PagingListAdapter.ListViewHolder>(
    AppsViewDiffUtil()
) {

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemListAppsBinding.bind(view)

        fun bind(item: AppsView, click: (AppsView) -> Unit) = binding.run {
            txtTitle.text = item.name
            txtSize.text = item.size.bytesToString()
            imgItem.loadUrl(item.icon, item.id.toString())

            root.setOnClickListener {
                click.invoke(item)
            }

            imgLabel.isVisible = item.iconLabel != null
            if (item.iconLabel != null) imgLabel.loadRes(item.iconLabel!!, item.id.toString())
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item, onClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = parent.inflate(R.layout.item_list_apps)
        return ListViewHolder(view)
    }

}