package com.utsman.listing.ui.activity

import android.os.Bundle
import android.view.Menu
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.utsman.listing.R
import com.utsman.listing.databinding.ActivitySearchBinding
import com.utsman.listing.viewmodel.SearchPagingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchAppActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by viewBinding()
    private val viewModel: SearchPagingViewModel by viewModel()
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.pagingData.observe(this, Observer { pagingData ->

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        menu?.findItem(R.id.search_action)?.also { searchMenu ->
            searchMenu.expandActionView()
            searchView = searchMenu.actionView as SearchView
            searchView?.run {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrBlank()) {
                            isIconified = false
                            clearFocus()
                            //viewModel.searchApps(query)
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }

                })
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (searchView?.isIconified == false) {
            searchView?.isIconified = true
        } else {
            super.onBackPressed()
        }
    }
}