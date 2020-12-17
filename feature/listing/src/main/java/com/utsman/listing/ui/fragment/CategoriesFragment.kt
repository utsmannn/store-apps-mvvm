/*
 * Created by Muhammad Utsman on 14/12/20 9:14 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.databinding.ItemListLoaderBinding
import com.utsman.abstraction.extensions.hideEmpty
import com.utsman.abstraction.extensions.initialLoadState
import com.utsman.listing.R
import com.utsman.listing.databinding.LayoutRecyclerViewBinding
import com.utsman.listing.ui.adapter.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.layout_recycler_view) {

    private val binding: LayoutRecyclerViewBinding? by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvList?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = CategoriesAdapter()
        }

        binding?.run {
            layoutProgress.progressCircular.isVisible = false
            layoutProgress.btnRetry.isVisible = false
            layoutProgress.txtMsg.isVisible = false
            layoutEmpty.hideEmpty()
        }

    }
}