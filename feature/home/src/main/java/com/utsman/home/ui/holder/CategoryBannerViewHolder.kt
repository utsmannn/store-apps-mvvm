package com.utsman.home.ui.holder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.intentTo
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.model.dto.list.CategorySealedView
import com.utsman.home.databinding.ItemAppsCategoryBannerBinding
import com.utsman.home.ui.adapter.AppsAdapter

class CategoryBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAppsCategoryBannerBinding.bind(view)

    fun bind(item: CategorySealedView.CategoryBannerView) = binding.run {
        val appsAdapter = AppsAdapter(isMini = true).apply {
            updateItems(item.apps)
        }

        txtTitle.text = item.name
        txtDesc.text = item.desc
        imgBanner.loadUrl(item.image, item.name)

        cardContainer.setOnClickListener {
            it.context.intentTo("com.utsman.listing.ui.activity.ListAppActivity") {
                putExtra("title", item.name)
                putExtra("query", item.query)
                putExtra("is_search", true)
            }
        }
        rvApps.run {
            layoutManager = GridLayoutManager(context, 5)
            adapter = appsAdapter
        }
    }
}