/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.base

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.R
import com.utsman.abstraction.databinding.ItemListLoaderBinding
import com.utsman.abstraction.extensions.inflate

class PagingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<PagingStateAdapter.PagingStateViewHolder>() {

    class PagingStateViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: PagingStateViewHolder, loadState: LoadState) {
        val binding = ItemListLoaderBinding.bind(holder.itemView)
        binding.run {
            progressCircular.isVisible = loadState is LoadState.Loading
            btnRetry.isVisible = loadState is LoadState.Error
            txtMsg.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                val msg = loadState.error.localizedMessage
                txtMsg.text = msg
            }

            btnRetry.setOnClickListener {
                retry.invoke()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingStateViewHolder {
        val view = parent.inflate(R.layout.item_list_loader)
        return PagingStateViewHolder(view)
    }
}