package com.utsman.listing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.utsman.data.model.dto.AppsView
import com.utsman.listing.domain.PagingUseCase
import kotlinx.coroutines.launch

class PagingViewModel(private val pagingUseCase: PagingUseCase) : ViewModel() {
    val pagingData: LiveData<PagingData<AppsView>> get() = pagingUseCase.pagingData

    fun searchApps(query: String? = null) = viewModelScope.launch {
        pagingUseCase.searchApps(this, query)
    }

    fun restartState() = viewModelScope.launch {
        pagingUseCase.restartState(this)
    }
}