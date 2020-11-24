package com.utsman.home.ui.holder

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.utsman.abstraction.ext.intentTo
import com.utsman.data.model.dto.CategoryView
import com.utsman.home.databinding.ItemAppsCategoryBinding
import com.utsman.home.ui.adapter.AppsAdapter

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAppsCategoryBinding.bind(view)
    fun bind(categoryView: CategoryView) = binding.run {
        val appsAdapter = AppsAdapter().apply {
            updateItems(categoryView.apps)
        }

        txtLabelCategory.text = categoryView.name
        txtLabelCategory.setOnClickListener {
            it.context.intentTo("com.utsman.listing.ui.ListActivity") {
                putExtra("title", categoryView.name)
                putExtra("query", categoryView.query)
            }
        }
        rvApps.run {
            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = appsAdapter
        }
    }
}