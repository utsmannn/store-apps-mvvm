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
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.utils.DataStoreUtils
import com.utsman.data.utils.DownloadUtils
import com.utsman.network.toAny
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

    @InternalCoroutinesApi
    override suspend fun doWork() = withContext(Dispatchers.IO) {
        callFunction(this)
    }

    @InternalCoroutinesApi
    private suspend fun callFunction(scope: CoroutineScope): Result = suspendCancellableCoroutine { task ->
        scope.launch {
            val fileString = inputData.getString("file")
            val file = fileString?.toAny(FileDownload::class.java)

            logi("file is ----> $file")
            val packageName = file?.packageName

            val downloadIdSaved = DataStoreUtils.getDownloadId(packageName)
            if (downloadIdSaved != null && DataStoreUtils.checkIsRun(packageName)) {
                task.observingDownload(downloadIdSaved, packageName)
            } else {
                val downloadId = DownloadUtils.startDownload(file)

                DataStoreUtils.markIsRun(packageName, downloadId)
                task.observingDownload(downloadId, packageName)
            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun CancellableContinuation<Result>.observingDownload(
        downloadId: Long?,
        packageName: String?
    ) {

        val task = this
        DownloadUtils.setDownloadListener(downloadId, object : DownloadUtils.DownloadListener {
            override suspend fun onSuccess(cursor: Cursor) {
                logi("success....")
                DataStoreUtils.removeApp(packageName)

                progress = 100
                finished = true

                if (task.isActive) {
                    task.resume(Result.success(doneData))
                } else {
                    task.tryResume(Result.success(doneData))
                }
            }

            override suspend fun onRunning(
                cursor: Cursor,
                fileSizeObserver: DownloadUtils.FileSizeObserver
            ) {
                logi("running")
                val size = fileSizeObserver.sizeReadable.total
                val soFar = fileSizeObserver.sizeReadable.soFar
                val progress = fileSizeObserver.sizeReadable.progress

                logi("size: $size | downloaded: $soFar | $progress")

                val progressData = workDataOf(
                    "progress" to fileSizeObserver.progress,
                    "data" to fileSizeObserver.convertToString()
                )
                setProgress(progressData)
            }

            override suspend fun onPaused(cursor: Cursor) {
                logi("paused....")
            }

            override suspend fun onPending(cursor: Cursor) {
                logi("pending...")
            }

            override suspend fun onFailed(cursor: Cursor) {
                logi("failed...")
                DataStoreUtils.removeApp(packageName)

                finished = true
                if (task.isActive) {
                    task.resume(Result.failure(doneData))
                } else {
                    task.tryResume(Result.failure(doneData))
                }
            }

            override suspend fun onCancel() {
                logi("cancel....")
                DataStoreUtils.removeApp(packageName)

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