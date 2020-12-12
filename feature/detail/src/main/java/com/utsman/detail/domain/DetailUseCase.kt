/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.domain

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.*
import com.utsman.abstraction.extensions.getValueOf
import com.utsman.abstraction.interactor.ResultState
import com.utsman.abstraction.interactor.fetch
import com.utsman.abstraction.interactor.stateOf
import com.utsman.abstraction.extensions.logi
import com.utsman.data.di._currentDownloadHelper
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.detail.toDetailView
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.model.dto.worker.WorkerAppsMap
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.worker.DownloadAppWorker
import com.utsman.network.toJson
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

    private val databaseHelper = getValueOf(_currentDownloadHelper)

    val detailView = stateOf<DetailView>()
    val workerState = MutableStateFlow<Operation.State?>(null)
    val workInfoState = MutableStateFlow<WorkInfoResult>(WorkInfoResult.Stopped())

    private fun getCurrentApps() = databaseHelper?.getCurrentAppsFlow()

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
        packageName: String,
        scope: CoroutineScope,
    ): Flow<WorkInfoResult> = channelFlow {
        offer(WorkInfoResult.Stopped())

        logi("try observing uuid...")
        scope.launch {
            if (getCurrentApps() != null) {
                getCurrentApps()!!
                    .mapNotNull {
                        it.find { a -> a.packageName == packageName }
                    }
                    .flatMapMerge { app ->
                        workManager.getWorkInfoByIdLiveData(UUID.fromString(app.uuid))
                            .asFlow()
                    }
                    .collect { workInfo ->
                        logi("work info in use case is -> $workInfo")

                        offer(WorkInfoResult.Downloading(workInfo, packageName))
                        if (workInfo != null) {
                            val progressData = workInfo.outputData.getBoolean("done", false)
                            logi("done data is -> $progressData")
                            if (progressData) {
                                offer(WorkInfoResult.Stopped())
                            }
                        } else {
                            logi("work info is null")
                            databaseHelper?.removeApp(packageName)
                            offer(WorkInfoResult.Stopped())
                        }
                    }
            }
        }

        awaitClose { cancel() }
    }

    suspend fun observerWorkInfoResult(
        scope: CoroutineScope,
        packageName: String
    ) = scope.launch {
        flowWorkResultBuilder(packageName, scope).collect {
            workInfoState.value = it
        }
    }

    suspend fun requestDownload(
        scope: CoroutineScope,
        file: FileDownload
    ) = scope.launch {

        val fileString = file.toJson()
        val inputData = workDataOf("file" to fileString)

        val worker = OneTimeWorkRequestBuilder<DownloadAppWorker>()
            .addTag(file.packageName ?: "")
            .setInputData(inputData)
            .build()

        val workerAppsMap = WorkerAppsMap(
            packageName = file.packageName ?: "",
            uuid = worker.id.toString(),
            name = file.name ?: ""
        )

        databaseHelper?.saveApp(scope, workerAppsMap)

        scope.launch {
            workManager.enqueue(worker)
                .state
                .asFlow()
                .collect {
                    workerState.value = it
                }
        }
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        detailView.value = ResultState.Idle()
    }
}