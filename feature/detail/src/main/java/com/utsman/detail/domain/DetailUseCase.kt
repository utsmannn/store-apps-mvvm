/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.domain

import androidx.lifecycle.asFlow
import androidx.work.*
import com.utsman.abstraction.dto.ResultState
import com.utsman.abstraction.dto.fetch
import com.utsman.abstraction.dto.stateOf
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.detail.toDetailView
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.worker.DownloadAppWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val metaRepository: MetaRepository,
    private val installedAppsRepository: InstalledAppsRepository,
    private val workManager: WorkManager
) {

    val detailView = stateOf<DetailView>()
    val workerState = MutableStateFlow<Operation.State?>(null)
    val tags = MutableStateFlow<MutableSet<String>?>(null)

    suspend fun getDetail(scope: CoroutineScope, packageName: String) = scope.launch {
        fetch {
            val response = metaRepository.getDetail(packageName)
            val detailView = response?.toDetailView() ?: DetailView()
            installedAppsRepository.checkInstalledApps(detailView)
        }.collect {
            detailView.value = it
        }
    }

    fun observerWorkInfo(scope: CoroutineScope, id: UUID) = run {
        val liveDataWorker = workManager.getWorkInfoByIdLiveData(id)
        scope.launch {
            liveDataWorker
                .asFlow()
                .collect { workInfo ->
                    if (workInfo != null) {
                        val data = workInfo.tags
                        tags.value = data
                    }
                }
        }

        liveDataWorker
    }

    fun clearTags(scope: CoroutineScope) = scope.launch {
        tags.value?.clear()
    }

    fun checkDownloading(packageName: String): Flow<Boolean> = flow {
        tags.collect { list ->
            val found = list?.find { tag -> tag == packageName }
            if (found != null) emit(true)
            else emit(false)
        }
    }

    fun requestDownload(scope: CoroutineScope, url: String, tag: String): UUID = run {
        val inputData = workDataOf("file_url" to url)
        val worker = OneTimeWorkRequestBuilder<DownloadAppWorker>()
            .addTag(tag)
            .setInputData(inputData)
            .build()

        scope.launch {
            workManager.enqueue(worker)
                .state
                .asFlow()
                .collect {
                    workerState.value = it
                }
        }

        worker.id
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        detailView.value = ResultState.Idle()
    }
}