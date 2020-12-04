/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.abstraction.dto.ResultState
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.detail.domain.DetailUseCase
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(private val detailUseCase: DetailUseCase) : ViewModel() {

    private val _detailView = detailUseCase.detailView
    val detailView: LiveData<ResultState<DetailView>>
        get() = _detailView.asLiveData(viewModelScope.coroutineContext)

    fun getDetailView(packageName: String) = viewModelScope.launch {
        detailUseCase.getDetail(this, packageName)
    }

    fun restartState() = viewModelScope.launch {
        detailUseCase.restartState(this)
    }
}