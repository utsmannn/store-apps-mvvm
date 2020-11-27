package com.utsman.listing.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.utsman.abstraction.base.PagingStateAdapter
import com.utsman.abstraction.ext.booleanExtras
import com.utsman.abstraction.ext.initialLoadState
import com.utsman.abstraction.ext.stringExtras
import com.utsman.listing.databinding.ActivityListBinding
import com.utsman.listing.ui.adapter.PagingListAdapter
import com.utsman.listing.viewmodel.PagingViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListAppActivity : AppCompatActivity() {

    private val binding: ActivityListBinding by viewBinding()
    private val viewModel: PagingViewModel by viewModel()

    private val query by stringExtras("query")
    private val title by stringExtras("title")
    private val isSearch by booleanExtras("is_search")

    private val pagingListAdapter = PagingListAdapter {

    }

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

        viewModel.getApps(query, isSearch)
        viewModel.pagingData.observe(this, Observer { pagingData ->
            GlobalScope.launch {
                pagingListAdapter.submitData(pagingData)
            }
        })

        pagingListAdapter.addLoadStateListener { combinedLoadStates ->
            binding.layoutProgress.initialLoadState(combinedLoadStates.refresh) {
                pagingListAdapter.retry()
            }

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