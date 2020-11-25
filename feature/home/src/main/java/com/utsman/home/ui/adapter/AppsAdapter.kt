package com.utsman.home.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.inflate
import com.utsman.data.model.dto.AppsSealedView
import com.utsman.data.model.dto.AppsSealedView.AppsBannerView
import com.utsman.data.model.dto.AppsSealedView.AppsView
import com.utsman.data.model.dto.AppsViewType
import com.utsman.home.R
import com.utsman.home.ui.holder.AppsBannerViewHolder
import com.utsman.home.ui.holder.AppsViewHolder

class AppsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val apps = mutableListOf<AppsSealedView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBanner = parent.inflate(R.layout.item_apps_banner)
        val viewRegular = parent.inflate(R.layout.item_apps)
        return when(AppsViewType.values()[viewType]) {
            AppsViewType.BANNER -> AppsBannerViewHolder(viewBanner)
            AppsViewType.REGULAR -> AppsViewHolder(viewRegular)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (val item = apps[position]) {
            is AppsBannerView -> {
                (holder as AppsBannerViewHolder).bind(item)
            }
            is AppsView -> {
                (holder as AppsViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    override fun getItemViewType(position: Int): Int {
        return apps[position].viewType.ordinal
    }

    fun updateItems(apps: List<AppsSealedView>) {
        this.apps.addAll(apps)
        notifyDataSetChanged()
    }
}