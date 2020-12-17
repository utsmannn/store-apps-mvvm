/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.extensions

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.utsman.abstraction.R
import com.utsman.abstraction.databinding.InitialLoaderBinding
import com.utsman.abstraction.databinding.ItemListEmptyBinding
import com.utsman.abstraction.databinding.ItemListLoaderBinding
import com.utsman.abstraction.interactor.ResultState

fun ItemListLoaderBinding.initialLoadState(state: LoadState, retry: () -> Unit) = run {
    progressCircular.isVisible = state is LoadState.Loading
    btnRetry.isVisible = state is LoadState.Error
    txtMsg.isVisible = state is LoadState.Error

    if (state is LoadState.Error) {
        val msg = state.error.localizedMessage
        txtMsg.text = msg
    }

    btnRetry.setOnClickListener {
        retry.invoke()
    }
}

fun ItemListEmptyBinding.initialEmptyState(
    state: LoadState,
    itemCount: Int = 0,
    imgRes: Int = R.drawable.ic_fluent_signed_24_filled,
    txtMessage: String = "Item empty"
) = run {
    logi("end paging is  -> ${state.endOfPaginationReached} | item count is -> $itemCount")
    if (state !is LoadState.Loading && itemCount == 0) {
        showEmpty(imgRes, txtMessage)
    } else {
        hideEmpty()
    }
}

fun <T : Any> ResultState<T>.bindToProgressView(
    binding: InitialLoaderBinding,
    parentView: View,
    retry: () -> Unit
) {
    binding.run {
        parentView.isVisible = this@bindToProgressView is ResultState.Success

        progressCircular.isVisible = this@bindToProgressView is ResultState.Loading
        btnRetry.isVisible = this@bindToProgressView is ResultState.Error
        txtMsg.isVisible = this@bindToProgressView is ResultState.Error

        if (this@bindToProgressView is ResultState.Error) {
            val msg = this@bindToProgressView.th.localizedMessage
            txtMsg.text = msg
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }
}



fun ItemListEmptyBinding.showEmpty(
    imgRes: Int = R.drawable.ic_fluent_signed_24_filled,
    txtMessage: String = "Item empty"
) {
    imgEmpty.isVisible = true
    txtMessageEmpty.isVisible = true

    val emptyDrawable = ContextCompat.getDrawable(imgEmpty.context, imgRes)
    imgEmpty.setImageDrawable(emptyDrawable)
    txtMessageEmpty.text = txtMessage
}

fun ItemListEmptyBinding.hideEmpty() {
    imgEmpty.isVisible = false
    txtMessageEmpty.isVisible = false
}