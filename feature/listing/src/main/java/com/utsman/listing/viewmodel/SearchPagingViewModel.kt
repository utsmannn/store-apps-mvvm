/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.domain.RecentQueryUseCase
import kotlinx.coroutines.launch

class SearchPagingViewModel @ViewModelInject constructor(
    private val pagingUseCase: PagingUseCase,
    private val queryUseCase: RecentQueryUseCase
) : ViewModel() {
    val pagingData: LiveData<PagingData<AppsSealedView.AppsView>> get() = pagingUseCase.pagingData
    val queries get() = queryUseCase.queries
        .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            queryUseCase.getRecentQuery()
        }
    }

    fun removeQuery(query: String) = viewModelScope.launch {
        queryUseCase.remove(query)
    }

    fun searchApps(query: String?) = viewModelScope.launch {
        if (!query.isNullOrEmpty()) {
            queryUseCase.insert(query)
            pagingUseCase.searchApps(this, query, true)
        }
    }

    fun restartState() = viewModelScope.launch {
        pagingUseCase.restartState(this)
    }
}