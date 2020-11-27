package com.utsman.listing.ui.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.ext.initialLoadState
import com.utsman.listing.R
import com.utsman.listing.databinding.ActivityListBinding
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class InstalledAppFragment : Fragment(R.layout.activity_list) {

    private val binding: ActivityListBinding by viewBinding()
    private val viewModel: InstalledAppsViewModel by viewModel()


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