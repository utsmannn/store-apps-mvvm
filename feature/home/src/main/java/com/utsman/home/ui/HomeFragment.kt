package com.utsman.home.ui

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.home.R
import com.utsman.home.databinding.FragmentHomeBinding
import com.utsman.home.ui.adapter.CategoryAdapter
import com.utsman.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModel()

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
                progressCircular.isVisible = combinedLoadStates.refresh is LoadState.Loading
            }
        }

        viewModel.getPagingCategories()
        viewModel.pagingCategories.observe(viewLifecycleOwner, Observer { pagingData ->
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    categoryAdapter.submitData(pagingData)
                }
            }
        })
    }
}