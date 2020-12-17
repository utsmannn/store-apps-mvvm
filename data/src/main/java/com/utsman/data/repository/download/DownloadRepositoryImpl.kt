/*
 * Created by Muhammad Utsman on 13/12/20 10:58 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.download

import androidx.lifecycle.asFlow
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import com.utsman.data.model.dto.worker.WorkerAppsMap
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.utils.DownloadUtils
import com.utsman.data.worker.DownloadAppWorker
import com.utsman.network.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
    private val downloadedRepository: DownloadedRepository
) : DownloadRepository {

    private suspend fun getCurrentApps() = downloadedRepository.getCurrentAppsSuspendFlow()

    override suspend fun requestDownload(scope: CoroutineScope, file: FileDownload): Flow<Operation.State> {
        val fileString = file.toJson()
        val inputData = workDataOf("file" to fileString)

        val worker = OneTimeWorkRequestBuilder<DownloadAppWorker>()
            .addTag(file.packageName ?: "")
            .setInputData(inputData)
            .build()

        val workerAppsMap = WorkerAppsMap(
            packageName = file.packageName ?: "",
            uuid = worker.id.toString(),
            name = file.name ?: "",
            fileName = file.fileName
        )

        downloadedRepository.saveApp(scope, workerAppsMap)
        return workManager.enqueue(worker)
            .state
            .asFlow()
    }

    override suspend fun cancelDownload(scope: CoroutineScope, downloadId: Long?) {
        DownloadUtils.cancel(scope, downloadId)
    }

    override suspend fun observerWorkInfo(scope: CoroutineScope, packageName: String): Flow<WorkInfoResult> {
        return channelFlow {
            offer(WorkInfoResult.Stopped())

            logi("try observing uuid...")
            scope.launch {
                getCurrentApps()
                    .mapNotNull {
                        it.find { a -> a?.packageName == packageName }
                    }
                    .flatMapMerge { app ->
                        workManager.getWorkInfoByIdLiveData(UUID.fromString(app.uuid))
                            .asFlow()
                    }
                    .collect { workInfo ->
                        logi("work info in use case is -> $workInfo")

                        offer(WorkInfoResult.Working(workInfo, packageName))
                        if (workInfo != null) {
                            val progressData = workInfo.outputData.getBoolean("done", false)
                            logi("done data is -> $progressData")
                            if (progressData) {
                                offer(WorkInfoResult.Stopped())
                            }
                        } else {
                            logi("work info null....................")
                        }
                    }
            }

            awaitClose { cancel() }
        }
    }
}