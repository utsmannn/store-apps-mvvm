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
import androidx.work.Operation
import com.utsman.abstraction.extensions.toBytesReadable
import com.utsman.abstraction.interactor.ResultState
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.utils.DownloadUtils
import com.utsman.detail.domain.DetailUseCase
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    private val detailUseCase: DetailUseCase
) :
    ViewModel() {

    private val _detailView = detailUseCase.detailView

    val detailView: LiveData<ResultState<DetailView>>
        get() = _detailView.asLiveData(viewModelScope.coroutineContext)

    val workerState: LiveData<Operation.State?>
        get() = detailUseCase.workerState.asLiveData(viewModelScope.coroutineContext)

    val workStateResult
        get() = detailUseCase.workInfoState.asLiveData(viewModelScope.coroutineContext)

    private fun getDataSync(): DetailView? {
        return detailUseCase.detailView.value.payload
    }

    fun getDetailView(packageName: String) = viewModelScope.launch {
        detailUseCase.getDetail(this, packageName)
    }

    fun requestDownload(fileDownload: FileDownload) = viewModelScope.launch {
        detailUseCase.requestDownload(viewModelScope, fileDownload)
    }

    fun cancelDownload(downloadId: Long?) = viewModelScope.launch {
        detailUseCase.cancelDownload(this, downloadId)
    }

    fun observerWorkInfo(packageName: String) = viewModelScope.launch {
        detailUseCase.observerWorkInfoResult(this, packageName)
    }

    fun getFileName(): String {
        val data = getDataSync()
        return "${data?.packageName}-${data?.appVersion?.apiCode}"
    }

    fun isUpdate(): Boolean {
        val data = getDataSync()
        return data?.appVersion?.run {
            code != 0L && apiCode > code
        } ?: false
    }

    fun isInstalled(): Boolean {
        val data = getDataSync()
        return DownloadUtils.checkAppIsInstalled(data?.packageName)
    }

    fun isDownloadedApk(): Boolean {
        return detailUseCase.checkDownloadedApks(getFileName())
    }

    fun getDownloadButtonTitle(): String {
        val data = getDataSync()
        val size = data?.file?.size?.toBytesReadable()
        return when {
            isUpdate() -> {
                "Update ($size)"
            }
            isInstalled() -> {
                "Open"
            }
            isDownloadedApk() -> {
                "Install"
            }
            else -> {
                "Download ($size)"
            }
        }
    }

    fun restartState() = viewModelScope.launch {
        detailUseCase.restartState(this)
    }
}