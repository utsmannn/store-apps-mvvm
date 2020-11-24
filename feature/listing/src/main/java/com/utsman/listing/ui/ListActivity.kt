package com.utsman.listing.ui

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.utsman.abstraction.di.moduleOf
import com.utsman.abstraction.ext.logi
import com.utsman.abstraction.ext.stringExtras
import com.utsman.listing.R
import com.utsman.listing.databinding.ActivityListBinding
import com.utsman.listing.di.pagingViewModel
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.ui.adapter.PagingStateAdapter
import com.utsman.listing.viewmodel.PagingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private val binding: ActivityListBinding by viewBinding()
    private val viewModel: PagingViewModel by moduleOf(pagingViewModel)

    private val query by stringExtras("query")
    private val title by stringExtras("title")

    private val pagingListAdapter = PagingListAdapter {

    }

    private val pagingStateAdapter = PagingStateAdapter {
        pagingListAdapter.retry()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = this@ListActivity.title
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

        viewModel.searchApps(query)
        viewModel.pagingData.observe(this, Observer { pagingData ->
            lifecycleScope.launch {
                pagingListAdapter.submitData(pagingData)
            }
        })

        pagingListAdapter.addLoadStateListener { combinedLoadStates ->
            binding.progressCircularInitial.isVisible = combinedLoadStates.refresh is LoadState.Loading
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.restartState()
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