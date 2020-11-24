package com.utsman.home.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.bytesToString
import com.utsman.abstraction.ext.inflate
import com.utsman.abstraction.ext.loadUrl
import com.utsman.data.model.dto.AppsView
import com.utsman.home.R
import com.utsman.home.databinding.ItemAppsBinding

class AppsAdapter : RecyclerView.Adapter<AppsAdapter.AppsViewHolder>() {

    class AppsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemAppsBinding.bind(view)

        fun bind(item: AppsView) = binding.run {
            txtTitle.text = item.name
            txtSize.text = item.size.bytesToString()
            imgItem.loadUrl(item.icon, item.id.toString())
        }
    }

    private val apps = mutableListOf<AppsView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        val view = parent.inflate(R.layout.item_apps)
        return AppsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        val item = apps[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    fun updateItems(apps: List<AppsView>) {
        this.apps.addAll(apps)
        notifyDataSetChanged()
    }
}