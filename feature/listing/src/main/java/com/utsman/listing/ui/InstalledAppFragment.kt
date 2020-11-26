package com.utsman.listing.ui

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.di.moduleOf
import com.utsman.data.model.dto.AppVersion
import com.utsman.listing.R
import com.utsman.listing.databinding.ActivityListBinding
import com.utsman.listing.di.installedAppViewModel
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import kotlinx.coroutines.launch

class InstalledAppFragment : Fragment(R.layout.activity_list) {

    private val binding: ActivityListBinding by viewBinding()
    private val viewModel: InstalledAppsViewModel by moduleOf(installedAppViewModel)


    private val pagingListAdapter = PagingListAdapter(isUpdatedApp = true) {

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
            binding.progressCircularInitial.isVisible = combinedLoadStates.refresh is LoadState.Loading
        }

        viewModel.getInstalledApps()
        viewModel.installedApps.observe(viewLifecycleOwner, Observer { pagingData ->
            lifecycleScope.launch {
                pagingListAdapter.submitData(pagingData)
            }
        })
    }
}