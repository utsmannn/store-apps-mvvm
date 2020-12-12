/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.extensions.initialLoadState
import com.utsman.listing.R
import com.utsman.listing.databinding.LayoutRecyclerViewBinding
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstalledAppFragment : Fragment(R.layout.layout_recycler_view) {

    private val binding: LayoutRecyclerViewBinding by viewBinding()
    private val viewModel: InstalledAppsViewModel by viewModels()

    private val pagingListAdapter =
        PagingListAdapter(holderType = PagingListAdapter.HolderType.UPDATED) {

        }

    private val pagingStateAdapter = PagingStateAdapter {
        pagingListAdapter.retry()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = LinearLayoutManager(context)

        binding.rvList.run {
            layoutManager = linearLayout
            adapter = pagingListAdapter.withLoadStateFooter(pagingStateAdapter)
        }

        pagingListAdapter.addLoadStateListener { combinedLoadStates ->
            binding.layoutProgress.initialLoadState(combinedLoadStates.refresh) {
                pagingListAdapter.retry()
            }
        }

        viewModel.getInstalledApps()
        viewModel.installedApps.observe(viewLifecycleOwner, Observer { pagingData ->
            GlobalScope.launch {
                pagingListAdapter.submitData(pagingData)
            }
        })
    }
}