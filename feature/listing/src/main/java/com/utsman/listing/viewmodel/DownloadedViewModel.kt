/*
 * Created by Muhammad Utsman on 13/12/20 2:17 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.viewmodel

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.listing.domain.DownloadedUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadedViewModel @ViewModelInject constructor(
    private val downloadedUseCase: DownloadedUseCase,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val STATE_RV = "state_rv_downloaded"
    }

    private var stateRecyclerView: Parcelable?
        get() = state.get(STATE_RV)
        set(value) {
            state.set(STATE_RV, value)
        }

    val downloadedList = downloadedUseCase
        .list.asLiveData(viewModelScope.coroutineContext)

    fun markIsDone(downloadedApps: DownloadedApps) = viewModelScope.launch {
        downloadedUseCase.markIsDone(downloadedApps)
    }

    fun onResumeRecyclerView(recyclerView: RecyclerView?) {
        if (stateRecyclerView != null) {
            recyclerView?.layoutManager?.onRestoreInstanceState(stateRecyclerView)
        }
    }

    fun onPausedRecyclerView(recyclerView: RecyclerView?) {
        stateRecyclerView = recyclerView?.layoutManager?.onSaveInstanceState()
    }
}