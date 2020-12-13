/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.domain

import androidx.work.Operation
import com.utsman.abstraction.interactor.ResultState
import com.utsman.abstraction.interactor.fetch
import com.utsman.abstraction.interactor.stateOf
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.detail.toDetailView
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.repository.download.DownloadRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.meta.MetaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val metaRepository: MetaRepository,
    private val installedAppsRepository: InstalledAppsRepository,
    private val downloadRepository: DownloadRepository
) {

    val detailView = stateOf<DetailView>()
    val workerState = MutableStateFlow<Operation.State?>(null)
    val workInfoState = MutableStateFlow<WorkInfoResult>(WorkInfoResult.Stopped())

    suspend fun getDetail(scope: CoroutineScope, packageName: String) = scope.launch {
        fetch {
            val response = metaRepository.getDetail(packageName)
            val detailView = response?.toDetailView() ?: DetailView()
            installedAppsRepository.checkInstalledApps(detailView)
        }.collect {
            detailView.value = it
        }
    }

    suspend fun observerWorkInfoResult(
        scope: CoroutineScope,
        packageName: String
    ) {
        downloadRepository.observerWorkInfo(scope, packageName).collect {
            workInfoState.value = it
        }
    }

    suspend fun requestDownload(scope: CoroutineScope, file: FileDownload) {
        downloadRepository.requestDownload(scope, file).collect {
            workerState.value = it
        }
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        detailView.value = ResultState.Idle()
    }
}