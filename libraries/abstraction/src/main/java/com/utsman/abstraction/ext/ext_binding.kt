/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.ext

import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.utsman.abstraction.databinding.ItemListLoaderBinding

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