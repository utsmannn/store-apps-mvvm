/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.Operation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.utsman.abstraction.dto.ResultState
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.detail.domain.DetailUseCase
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel @ViewModelInject constructor(private val detailUseCase: DetailUseCase) :
    ViewModel() {

    private val _detailView = detailUseCase.detailView

    val detailView: LiveData<ResultState<DetailView>>
        get() = _detailView.asLiveData(viewModelScope.coroutineContext)

    val workerState: LiveData<Operation.State?>
        get() = detailUseCase.workerState.asLiveData(viewModelScope.coroutineContext)

    fun getDetailView(packageName: String) = viewModelScope.launch {
        detailUseCase.getDetail(this, packageName)
    }

    fun requestDownload(fileUrl: String, packageName: String) = run {
        detailUseCase.requestDownload(viewModelScope, fileUrl, packageName)
    }

    fun checkIsDownloading(packageName: String) = detailUseCase.checkDownloading(packageName)
        .asLiveData(viewModelScope.coroutineContext)

    fun observerWorkInfo(id: UUID) = detailUseCase.observerWorkInfo(viewModelScope, id)

    fun downloadIsComplete() = viewModelScope.launch {
        detailUseCase.clearTags(this)
    }

    fun restartState() = viewModelScope.launch {
        detailUseCase.restartState(this)
    }
}