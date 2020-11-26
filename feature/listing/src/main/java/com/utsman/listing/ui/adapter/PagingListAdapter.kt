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
import com.utsman.listing.databinding.ItemListUpdatedBinding

class PagingListAdapter(
    private val isUpdatedApp: Boolean = false,
    private val onClick: (AppsView) -> Unit
) : PagingDataAdapter<AppsView, RecyclerView.ViewHolder>(
    AppsViewDiffUtil()
) {

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemListAppsBinding.bind(view)

        fun bind(item: AppsView, click: (AppsView) -> Unit) = binding.run {
            txtTitle.text = item.name
            txtSize.text = item.size.bytesToString()
            imgItem.loadUrl(item.icon, item.id.toString())

            imgLabel.isVisible = item.iconLabel != null
            if (item.iconLabel != null) imgLabel.loadRes(item.iconLabel!!, item.id.toString())

            root.setOnClickListener {
                click.invoke(item)
            }
        }
    }

    class ListUpdateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemListUpdatedBinding.bind(view)

        fun bind(item: AppsView, click: (AppsView) -> Unit) = binding.run {
            txtTitle.text = item.name
            txtSize.text = item.size.bytesToString()
            imgItem.loadUrl(item.icon, item.id.toString())

            val versionString =
                "Current ver. ${item.appVersion.name}\nUpdated ver. ${item.appVersion.apiName}"
            txtVersion.text = versionString

            root.setOnClickListener {
                click.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) if (isUpdatedApp) {
            (holder as ListUpdateViewHolder).bind(item, onClick)
        } else {
            (holder as ListViewHolder).bind(item, onClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewRegular = parent.inflate(R.layout.item_list_apps)
        val viewUpdated = parent.inflate(R.layout.item_list_updated)
        return if (isUpdatedApp) {
            ListUpdateViewHolder(viewUpdated)
        } else {
            ListViewHolder(viewRegular)
        }
    }

}