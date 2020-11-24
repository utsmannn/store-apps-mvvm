package com.utsman.home.ui.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.utsman.abstraction.ext.inflate
import com.utsman.abstraction.ext.intentTo
import com.utsman.data.model.dto.CategoryView
import com.utsman.home.R
import com.utsman.home.databinding.ItemAppsCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.HomeViewHolder>() {

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

    private val categories = mutableListOf<CategoryView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val bind = parent.inflate(R.layout.item_apps_category)
        return HomeViewHolder(bind)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = categories[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun addItem(categoryView: CategoryView) {
        this.categories.add(categoryView)
        notifyDataSetChanged()
    }

    fun updateItems(categories: List<CategoryView>) {
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }
}