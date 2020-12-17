/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.extensions.booleanExtras
import com.utsman.abstraction.extensions.initialEmptyState
import com.utsman.abstraction.extensions.initialLoadState
import com.utsman.abstraction.extensions.stringExtras
import com.utsman.listing.R
import com.utsman.listing.databinding.LayoutRecyclerViewBinding
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.viewmodel.PagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListAppActivity : AppCompatActivity() {

    private val binding: LayoutRecyclerViewBinding by viewBinding()
    private val viewModel: PagingViewModel by viewModels()

    private val query by stringExtras("query")
    private val title by stringExtras("title")
    private val isSearch by booleanExtras("is_search")

    private val pagingListAdapter = PagingListAdapter(lifecycleOwner = this)

    private val pagingStateAdapter = PagingStateAdapter {
        pagingListAdapter.retry()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = this@ListAppActivity.title
        }

        val gridLayout = GridLayoutManager(this, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (pagingStateAdapter.loadState) {
                        is LoadState.NotLoading -> 1
                        is LoadState.Loading -> if (position == pagingListAdapter.itemCount) 3 else 1
                        is LoadState.Error -> if (position == pagingListAdapter.itemCount) 3 else 1
                        else -> 1
                    }
                }
            }
        }

        binding.rvList.run {
            layoutManager = gridLayout
            adapter = pagingListAdapter.withLoadStateFooter(pagingStateAdapter)
        }

        viewModel.restartState()
        viewModel.getApps(query, isSearch)
        viewModel.pagingData.observe(this, Observer { pagingData ->
            lifecycleScope.launch {
                pagingListAdapter.submitData(pagingData)
            }
        })

        pagingListAdapter.addLoadStateListener { combinedLoadStates ->
            val txtMessage = "Application not found"
            binding.layoutEmpty.initialEmptyState(
                combinedLoadStates.refresh,
                txtMessage = txtMessage,
                imgRes = R.drawable.ic_fluent_emoji_meh_24_regular,
                itemCount = pagingListAdapter.itemCount
            )

            binding.layoutProgress.initialLoadState(combinedLoadStates.refresh) {
                pagingListAdapter.retry()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}