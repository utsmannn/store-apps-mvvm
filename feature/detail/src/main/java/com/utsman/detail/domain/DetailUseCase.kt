/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.domain

import androidx.lifecycle.asFlow
import androidx.work.*
import com.utsman.abstraction.interactor.ResultState
import com.utsman.abstraction.interactor.fetch
import com.utsman.abstraction.interactor.stateOf
import com.utsman.abstraction.ext.logi
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.detail.toDetailView
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.store.CurrentWorkerPreferences
import com.utsman.data.worker.DownloadAppWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
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

    private suspend fun flowWorkResultBuilder(
        preferences: CurrentWorkerPreferences,
        packageName: String
    ): Flow<WorkInfoResult> = channelFlow {
        offer(WorkInfoResult.Stopped())

        preferences.currentPackage
            .collect { current ->
                preferences.currentUUID.collect { uuid ->
                    workManager.getWorkInfoByIdLiveData(UUID.fromString(uuid))
                        .asFlow()
                        .collect { workInfo ->
                            when {
                                current == packageName -> {
                                    offer(WorkInfoResult.Running(workInfo, current))
                                    val doneData = workInfo.outputData.getBoolean("done", false)
                                    logi("done is ---> $doneData")
                                    if (doneData) {
                                        preferences.clearCurrentPackage()
                                        offer(WorkInfoResult.Stopped())
                                    }
                                }
                                current.isEmpty() -> {
                                    logi("stopped")
                                    offer(WorkInfoResult.Stopped())
                                }
                                else -> {
                                    logi("waiting")
                                    offer(WorkInfoResult.Waiting())
                                }
                            }
                        }
                }
            }

        /*preferences.run {
            var currentPackageValue = ""
            currentPackage
                .flatMapConcat {
                    currentPackageValue = it
                    currentUUID
                }
                .flatMapMerge {
                    workManager.getWorkInfoByIdLiveData(UUID.fromString(it)).asFlow()
                }
                .collect {
                    logi("current is -> || $currentPackageValue ||")
                    offer(WorkInfoResult.Running(it, currentPackageValue))
                    val doneData = it.outputData.getBoolean("done", false)
                    logi("done is ---> $doneData")
                    if (doneData) {
                        clearCurrentPackage()
                        offer(WorkInfoResult.Stopped())
                    }
                }
        }*/

        awaitClose { cancel() }
    }

    suspend fun observerWorkInfoResult(
        scope: CoroutineScope,
        preferences: CurrentWorkerPreferences,
        packageName: String
    ) = scope.launch {
        flowWorkResultBuilder(preferences, packageName).collect {
            workInfoState.value = it
        }
    }

    fun requestDownload(
        scope: CoroutineScope,
        store: CurrentWorkerPreferences,
        url: String,
        tag: String
    ): UUID = run {
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
                    /*when (it) {
                        is Operation.State.IN_PROGRESS ->
                    }*/
                    logi("worker status -----> $it")
                    workerState.value = it
                }
        }

        worker.id
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        detailView.value = ResultState.Idle()
    }
}