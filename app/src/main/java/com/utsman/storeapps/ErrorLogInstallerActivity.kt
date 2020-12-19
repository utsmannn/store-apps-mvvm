/*
 * Created by Muhammad Utsman on 20/12/20 4:55 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.utsman.abstraction.extensions.hideEmpty
import com.utsman.abstraction.extensions.showEmpty
import com.utsman.abstraction.extensions.snackbar
import com.utsman.listing.databinding.LayoutRecyclerViewBinding
import com.utsman.storeapps.adapter.ErrorLogInstallerAdapter
import com.utsman.storeapps.viewmodel.ErrorLogInstallerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorLogInstallerActivity : AppCompatActivity() {

    private val binding: LayoutRecyclerViewBinding by viewBinding()
    private val errorLogAdapter = ErrorLogInstallerAdapter()
    private val viewModel: ErrorLogInstallerViewModel by viewModels()


    private val dialogBuilder by lazy {
        AlertDialog.Builder(this)
            .setTitle("Clear")
            .setMessage("Clear error log installer?")
            .setPositiveButton("Clear") { v, i ->
                viewModel.clearLog()
                finish()
                v.dismiss()
            }
            .setNegativeButton("Cancel") { v, i ->
                v.dismiss()
            }
    }

    private val confirmClearDelete by lazy {
        dialogBuilder.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.restartState()

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            chipQuery.isVisible = false
            layoutProgress.run {
                txtMsg.isVisible = false
                progressCircular.isVisible = false
                btnRetry.isVisible = false
            }

            rvList.run {
                layoutManager = LinearLayoutManager(this@ErrorLogInstallerActivity)
                addItemDecoration(DividerItemDecoration(this@ErrorLogInstallerActivity, OrientationHelper.VERTICAL))
                adapter = errorLogAdapter
            }

            viewModel.errorLog.observe(this@ErrorLogInstallerActivity, Observer { errors ->
                if (errors.isEmpty()) {
                    val imgRes = R.drawable.ic_fluent_emoji_laugh_24_filled
                    layoutEmpty.showEmpty(imgRes = imgRes, txtMessage = "No error found")
                } else {
                    layoutEmpty.hideEmpty()
                    errorLogAdapter.addList(errors)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.error_log_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_action -> {
                if (errorLogAdapter.itemCount == 0) {
                    snackbar("No need cleared")
                } else {
                    confirmClearDelete.show()
                }
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}