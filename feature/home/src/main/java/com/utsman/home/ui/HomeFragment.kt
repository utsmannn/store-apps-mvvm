/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.ui

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.ext.initialLoadState
import com.utsman.home.R
import com.utsman.home.databinding.FragmentHomeBinding
import com.utsman.home.ui.adapter.CategoryAdapter
import com.utsman.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()

    private val categoryAdapter = CategoryAdapter()
    private val stateAdapter = PagingStateAdapter {
        categoryAdapter.retry()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            rvHome.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = categoryAdapter.withLoadStateFooter(stateAdapter)
            }

            categoryAdapter.addLoadStateListener { combinedLoadStates ->
                binding.layoutProgress.initialLoadState(combinedLoadStates.refresh) {
                    categoryAdapter.retry()
                }
            }
        }

        viewModel.getPagingCategories()
        viewModel.pagingCategories.observe(viewLifecycleOwner, Observer { pagingData ->
            lifecycleScope.launch {
                categoryAdapter.submitData(pagingData)
            }
        })
    }
}