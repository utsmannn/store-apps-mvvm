/*
 * Created by Muhammad Utsman on 13/12/20 2:22 AM
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.listing.R
import com.utsman.listing.databinding.LayoutRecyclerViewBinding
import com.utsman.listing.ui.adapter.DownloadedListAdapter
import com.utsman.listing.viewmodel.DownloadedViewModel
import com.utsman.network.toJson
import com.utsman.network.toJsonList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadedFragment : Fragment(R.layout.layout_recycler_view) {

    private val binding: LayoutRecyclerViewBinding? by viewBinding()
    private val viewModel: DownloadedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val downloadedAdapter = DownloadedListAdapter(viewLifecycleOwner)
        downloadedAdapter.markIsDone { downloaded ->
            viewModel.markIsDone(downloaded)
        }

        binding?.rvList?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = downloadedAdapter
        }

        hideProgress()
        viewModel.downloadedList.observe(viewLifecycleOwner, Observer { apps ->
            logi("apps is -> $apps")
            downloadedAdapter.updateList(apps)
        })
    }

    private fun hideProgress() = binding?.layoutProgress?.run {
        txtMsg.isVisible = false
        btnRetry.isVisible = false
        progressCircular.isVisible = false
    }
}