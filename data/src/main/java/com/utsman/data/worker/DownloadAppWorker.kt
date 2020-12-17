/*
 * Created by Muhammad Utsman on 4/12/20 11:41 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.worker

import android.content.Context
import android.database.Cursor
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.utsman.abstraction.extensions.getValueSafeOf
import com.utsman.abstraction.extensions.logi
import com.utsman.data.di._downloadedRepository
import com.utsman.data.model.dto.downloaded.Download
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.utils.DownloadUtils
import com.utsman.network.toAny
import com.utsman.network.toJson
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class DownloadAppWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    init {
        logi("creating work manager ....")
    }

    private var finished = false
    private var progress = 0L
    private val doneData = workDataOf("done" to true)
    private val databaseHelper = getValueSafeOf(_downloadedRepository)

    @InternalCoroutinesApi
    override suspend fun doWork() = withContext(Dispatchers.IO) {
        callFunction(this)
    }

    @InternalCoroutinesApi
    private suspend fun callFunction(scope: CoroutineScope): Result =
        suspendCancellableCoroutine { task ->
            scope.launch {
                val fileString = inputData.getString("file")
                val file = fileString?.toAny(FileDownload::class.java)

                logi("file is ----> $file")
                val packageName = file?.packageName

                val downloadIsRunForApp = databaseHelper?.checkIsRun(packageName) ?: false
                logi("download is run for ${file?.name} -> $downloadIsRunForApp")

                if (downloadIsRunForApp) {
                    val downloadIdSaved = databaseHelper?.getDownloadId(packageName)
                    databaseHelper?.markIsRun(this, packageName, downloadIdSaved)
                    task.observingDownload(this, downloadIdSaved, packageName)
                } else {
                    val downloadId = DownloadUtils.startDownload(file)
                    databaseHelper?.markIsRun(this, packageName, downloadId)
                    task.observingDownload(this, downloadId, packageName)
                }
            }
        }

    @InternalCoroutinesApi
    private suspend fun CancellableContinuation<Result>.observingDownload(
        scope: CoroutineScope,
        downloadId: Long?,
        packageName: String?
    ) {

        val progressPreparing = workDataOf("status_string" to "Preparing")
        setProgress(progressPreparing)

        val task = this
        DownloadUtils.setDownloadListener(downloadId, object : DownloadUtils.DownloadListener {
            override suspend fun onSuccess(cursor: Cursor, status: Download.Status) {
                val statusString = "Success"
                val statusJson = status.toJson()

                logi("status json is --> $statusJson")

                val progressData = workDataOf(
                    "status_string" to statusString,
                    "status" to statusJson
                )

                setProgress(progressData)

                logi("success....")
                databaseHelper?.markIsComplete(scope, packageName, downloadId)

                progress = 100
                finished = true

                if (task.isActive) {
                    task.resume(Result.success(doneData))
                } else {
                    task.tryResume(Result.success(doneData))
                }
            }

            override suspend fun onRunning(
                fileSizeObserver: DownloadUtils.FileSizeObserver?,
                status: Download.Status
            ) {
                val statusJson = status.toJson()
                logi("status json is --> $statusJson")

                if (fileSizeObserver != null) {
                    logi("running")
                    val size = fileSizeObserver.sizeReadable.total
                    val soFar = fileSizeObserver.sizeReadable.soFar
                    val progress = fileSizeObserver.sizeReadable.progress

                    val statusString = "$soFar downloaded of $size ($progress)"

                    val progressData = workDataOf(
                        "progress" to fileSizeObserver.progress,
                        "data" to fileSizeObserver.convertToString(),
                        "status_string" to statusString,
                        "status" to statusJson
                    )
                    setProgress(progressData)
                } else {
                    val progressData = workDataOf(
                        "status" to statusJson
                    )
                    setProgress(progressData)
                }
            }

            override suspend fun onPaused(cursor: Cursor, status: Download.Status) {
                logi("paused....")
                val statusString = "Paused"
                val statusJson = status.toJson()
                val progressData = workDataOf(
                    "status_string" to statusString,
                    "status" to statusJson
                )
                setProgress(progressData)
            }

            override suspend fun onPending(cursor: Cursor, status: Download.Status) {
                logi("pending...")
                val statusString = "Pending..."
                val statusJson = status.toJson()
                val progressData = workDataOf(
                    "status_string" to statusString,
                    "status" to statusJson
                )
                setProgress(progressData)
            }

            override suspend fun onFailed(cursor: Cursor, status: Download.Status) {
                logi("failed...")
                val statusString = "Failed"
                val statusJson = status.toJson()
                val progressData = workDataOf(
                    "status_string" to statusString,
                    "status" to statusJson
                )
                setProgress(progressData)
                databaseHelper?.removeApp(scope, packageName)

                finished = true
                if (task.isActive) {
                    task.resume(Result.failure(doneData))
                } else {
                    task.tryResume(Result.failure(doneData))
                }
            }

            override suspend fun onCancel(status: Download.Status) {
                logi("cancel....")
                val statusString = "Canceling..."
                val statusJson = status.toJson()
                val progressData = workDataOf(
                    "status_string" to statusString,
                    "status" to statusJson
                )
                setProgress(progressData)

                delay(1000)
                val statusString2 = "Cancel"
                val progressData2 = workDataOf("status_string" to statusString2)
                setProgress(progressData2)

                databaseHelper?.markIsComplete(scope, packageName, downloadId)
                databaseHelper?.removeApp(scope, packageName)

                progress = 100
                finished = true

                if (task.isActive) {
                    task.resume(Result.success(doneData))
                } else {
                    task.tryResume(Result.success(doneData))
                }
            }

        })
    }
}