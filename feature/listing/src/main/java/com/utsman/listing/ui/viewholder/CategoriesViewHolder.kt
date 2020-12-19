/*
 * Created by Muhammad Utsman on 14/12/20 9:16 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.extensions.intentTo
import com.utsman.abstraction.extensions.loadRes
import com.utsman.data.model.dto.list.Category
import com.utsman.listing.databinding.ItemCategoryBinding

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemCategoryBinding.bind(view)

    fun bind(category: Category) = binding.run {
        imgCategory.loadRes(category.iconRes, category.query)
        txtTitleCategory.text = category.name

        root.setOnClickListener {
            it.context.intentTo("com.utsman.listing.ui.activity.ListAppActivity") {
                putExtra("title", category.name)
                putExtra("query", category.query)
            }
        }
    }
}