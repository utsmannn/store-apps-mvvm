package com.utsman.home.ui

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
import com.utsman.home.R
import com.utsman.home.databinding.FragmentHomeBinding
import com.utsman.home.di.homeViewModel
import com.utsman.home.ui.adapter.PagingCategoryAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel by moduleOf(homeViewModel)

    private val pagingCategoryAdapter = PagingCategoryAdapter()
    private val pagingStateAdapter = PagingStateAdapter {
        pagingCategoryAdapter.retry()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pagingCategoryAdapter.withLoadStateFooter(pagingStateAdapter)
        }

        pagingCategoryAdapter.addLoadStateListener { combinedLoadStates ->
            binding.progressCircular.isVisible = combinedLoadStates.refresh is LoadState.Loading
        }

        viewModel.getPagingCategories()
        viewModel.pagingCategories.observe(viewLifecycleOwner, Observer { pagingData ->
            lifecycleScope.launch {
                pagingCategoryAdapter.submitData(pagingData)
            }
        })
    }
}