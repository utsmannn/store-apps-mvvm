package com.utsman.listing.ui

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.utsman.abstraction.di.moduleOf
import com.utsman.listing.databinding.ActivityListBinding
import com.utsman.listing.di.pagingViewModel
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.ui.adapter.PagingStateAdapter
import com.utsman.listing.viewmodel.PagingViewModel
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private val binding: ActivityListBinding by viewBinding()
    private val viewModel: PagingViewModel by moduleOf(pagingViewModel)

    private val pagingListAdapter = PagingListAdapter {

    }

    private val pagingStateAdapter = PagingStateAdapter {
        pagingListAdapter.retry()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridLayout = GridLayoutManager(this, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (pagingStateAdapter.loadState) {
                        is LoadState.NotLoading -> 1
                        is LoadState.Loading -> if (position == pagingListAdapter.itemCount) 2 else 1
                        is LoadState.Error -> if (position == pagingListAdapter.itemCount) 2 else 1
                        else -> 1
                    }
                }
            }
        }

        binding.run {
            rvList.apply {
                layoutManager = gridLayout
                adapter = pagingListAdapter.withLoadStateFooter(pagingStateAdapter)
            }
        }

        viewModel.searchApps("communication")
        viewModel.pagingData.observe(this, Observer { pagingData ->
            lifecycleScope.launch {
                pagingListAdapter.submitData(pagingData)
            }
        })
    }
}