/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.extensions.initialEmptyState
import com.utsman.abstraction.extensions.initialLoadState
import com.utsman.abstraction.extensions.toast
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.listing.R
import com.utsman.listing.databinding.LayoutRecyclerViewBinding
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstalledAppFragment : Fragment(R.layout.layout_recycler_view) {

    private val binding: LayoutRecyclerViewBinding? by viewBinding()
    private val viewModel: InstalledAppsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = LinearLayoutManager(context)

        binding?.run {
            val pagingListAdapter = PagingListAdapter(
                holderType = PagingListAdapter.HolderType.UPDATED,
                lifecycleOwner = viewLifecycleOwner
            )

            val pagingStateAdapter = PagingStateAdapter {
                pagingListAdapter.retry()
            }

            binding?.chipQuery?.isVisible = false
            rvList.run {
                layoutManager = linearLayout
                adapter = pagingListAdapter.withLoadStateFooter(pagingStateAdapter)
            }

            pagingListAdapter.onUpdateClick { data ->
                val fileName = "${data.packageName}-${data.appVersion.apiCode}"
                val fileDownload = FileDownload.simple {
                    this.id = data.id
                    this.name = data.name
                    this.fileName = fileName
                    this.packageName = data.packageName
                    this.url = data.appVersion.pathUrl
                }
                viewModel.requestDownload(fileDownload)
                context?.toast("${data.appVersion.pathUrl}")
            }

            pagingListAdapter.addLoadStateListener { combinedLoadStates ->

                val emptyMessage = "No application update"
                layoutEmpty.initialEmptyState(
                    combinedLoadStates,
                    pagingListAdapter.itemCount,
                    R.drawable.ic_fluent_signed_24_filled,
                    emptyMessage
                )

                layoutProgress.initialLoadState(combinedLoadStates.refresh) {
                    pagingListAdapter.retry()
                }
            }

            viewModel.installedApps.observe(viewLifecycleOwner, Observer { pagingData ->
                GlobalScope.launch {
                    pagingListAdapter.submitData(pagingData)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResumeRecyclerView(binding?.rvList)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPausedRecyclerView(binding?.rvList)
    }
}