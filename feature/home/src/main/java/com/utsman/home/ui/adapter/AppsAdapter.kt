/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.inflate
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.data.model.dto.list.AppsSealedView.AppsBannerView
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.data.model.dto.list.AppsViewType
import com.utsman.home.R
import com.utsman.home.ui.holder.AppsBannerViewHolder
import com.utsman.home.ui.holder.AppsMiniViewHolder
import com.utsman.home.ui.holder.AppsViewHolder

class AppsAdapter(private val isMini: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val apps = mutableListOf<AppsSealedView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBanner = parent.inflate(R.layout.item_apps_banner)
        val viewRegular = parent.inflate(R.layout.item_apps)
        val viewMini = parent.inflate(R.layout.item_apps_mini)
        return when(AppsViewType.values()[viewType]) {
            AppsViewType.BANNER -> AppsBannerViewHolder(viewBanner)
            AppsViewType.REGULAR -> {
                if (isMini) {
                    AppsMiniViewHolder(viewMini)
                } else {
                    AppsViewHolder(viewRegular)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = apps[position]) {
            is AppsBannerView -> {
                (holder as AppsBannerViewHolder).bind(item)
            }
            is AppsView -> {
                if (isMini) {
                    (holder as AppsMiniViewHolder).bind(item)
                } else {
                    (holder as AppsViewHolder).bind(item)
                }
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