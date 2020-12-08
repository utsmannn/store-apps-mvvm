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
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.model.dto.worker.WorkerAppsMap
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
    private val workManager: WorkManager,
    private val workerPreferences: CurrentWorkerPreferences
) {

    val detailView = stateOf<DetailView>()
    val workerState = MutableStateFlow<Operation.State?>(null)

    val workInfoState = MutableStateFlow<WorkInfoResult>(WorkInfoResult.Stopped())

    val currentApps = workerPreferences.currentApps

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
        preferences.currentApps
            .mapNotNull { i -> i.find { a -> a.packageName == packageName } }
            .flatMapMerge { app ->
                workManager.getWorkInfoByIdLiveData(UUID.fromString(app.uuid))
                    .asFlow()
            }
            .collect { workInfo ->
                offer(WorkInfoResult.Downloading(workInfo, packageName))

                val progressData = workInfo.outputData.getBoolean("done", false)
                logi("done data is -> $progressData")
                if (progressData) {
                    preferences.removeApp(packageName)
                    offer(WorkInfoResult.Stopped())
                }
            }

        awaitClose { cancel() }
    }

    suspend fun observerWorkInfoResult(
        scope: CoroutineScope,
        packageName: String
    ) = scope.launch {
        flowWorkResultBuilder(workerPreferences, packageName).collect {
            workInfoState.value = it
        }
    }

    suspend fun requestDownload(
        scope: CoroutineScope,
        file: FileDownload
    ) = scope.launch {
        val inputData = workDataOf(
            "file_url" to file.url,
            "name" to file.name,
            "file_name" to file.fileName
        )
        val worker = OneTimeWorkRequestBuilder<DownloadAppWorker>()
            .addTag(file.packageName ?: "")
            .setInputData(inputData)
            .build()

        val workerAppsMap = WorkerAppsMap(
            uuid = worker.id.toString(),
            packageName = file.packageName ?: "",
            name = file.name ?: ""
        )
        workerPreferences.saveApp(workerAppsMap)

        scope.launch {
            workManager.enqueue(worker)
                .state
                .asFlow()
                .collect {
                    workerState.value = it
                    logi("state is --> $it")
                    when (it) {
                        is Operation.State.FAILURE -> {
                            file.packageName?.let { p ->
                                workerPreferences.removeApp(p)
                            }
                        }
                    }
                }
        }
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        detailView.value = ResultState.Idle()
    }
}