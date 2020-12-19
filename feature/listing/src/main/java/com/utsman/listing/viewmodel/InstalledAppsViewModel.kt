/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.viewmodel

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.listing.domain.InstalledAppUseCase
import kotlinx.coroutines.launch

class InstalledAppsViewModel @ViewModelInject constructor(
    private val installedAppUseCase: InstalledAppUseCase,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val STATE_RV = "state_rv_installed"
    }

    private var stateRecyclerView: Parcelable?
        get() = state.get(STATE_RV)
        set(value) {
            state.set(STATE_RV, value)
        }

    private val _installedApps = installedAppUseCase.pagingData
    val installedApps: LiveData<PagingData<AppsSealedView.AppsView>>
        get() = _installedApps

    init {
        viewModelScope.launch {
            installedAppUseCase.getUpdatedApp()
        }
    }

    fun requestDownload(fileDownload: FileDownload) = viewModelScope.launch {
        installedAppUseCase.requestDownload(fileDownload)
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