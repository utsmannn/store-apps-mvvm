package com.utsman.listing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.listing.domain.PagingUseCase
import kotlinx.coroutines.launch

class SearchPagingViewModel(private val pagingUseCase: PagingUseCase) : ViewModel() {
    val pagingData: LiveData<PagingData<AppsSealedView.AppsView>> get() = pagingUseCase.pagingData

    fun searchApps(query: String?) = viewModelScope.launch {
        pagingUseCase.searchApps(this, query, true)
    }
}