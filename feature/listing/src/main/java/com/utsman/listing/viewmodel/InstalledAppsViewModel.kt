package com.utsman.listing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.utsman.data.model.dto.AppsSealedView
import com.utsman.listing.domain.InstalledAppUseCase
import kotlinx.coroutines.launch

class InstalledAppsViewModel(private val installedAppUseCase: InstalledAppUseCase) : ViewModel() {

    private val _installedApps = installedAppUseCase.pagingData
    val installedApps: LiveData<PagingData<AppsSealedView.AppsView>> get() = installedAppUseCase
        .pagingData

    fun getInstalledApps() = viewModelScope.launch {
        installedAppUseCase.getUpdatedApp(this)
    }
}