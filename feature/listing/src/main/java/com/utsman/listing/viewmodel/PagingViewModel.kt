/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.listing.domain.PagingUseCase
import kotlinx.coroutines.launch

class PagingViewModel @ViewModelInject constructor(private val pagingUseCase: PagingUseCase) : ViewModel() {
    val pagingData: LiveData<PagingData<AppsView>> get() = pagingUseCase.pagingData

    fun getApps(query: String? = null, isSearch: Boolean = false) = viewModelScope.launch {
        pagingUseCase.searchApps(this, query, isSearch)
    }

    fun restartState() = viewModelScope.launch {
        pagingUseCase.restartState(this)
    }
}