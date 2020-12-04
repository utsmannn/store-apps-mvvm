/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.ext

import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.utsman.abstraction.databinding.InitialLoaderBinding
import com.utsman.abstraction.databinding.ItemListLoaderBinding
import com.utsman.abstraction.dto.ResultState

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

fun <T : Any>ResultState<T>.bindToProgressView(binding: InitialLoaderBinding, parentView: View, retry: () -> Unit) {
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