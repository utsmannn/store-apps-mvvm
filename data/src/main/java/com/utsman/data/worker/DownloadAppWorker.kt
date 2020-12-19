/*
 * Created by Muhammad Utsman on 4/12/20 11:41 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.worker

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.utsman.abstraction.extensions.getValueSafeOf
import com.utsman.abstraction.extensions.logi
import com.utsman.data.R
import com.utsman.data.const.StringValues
import com.utsman.data.di._downloadedRepository
import com.utsman.data.di._rootRepository
import com.utsman.data.di._optionRepository
import com.utsman.data.model.dto.downloaded.Download
import com.utsman.data.model.dto.setting.SettingData
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.utils.DownloadUtils
import com.utsman.network.toAny
import com.utsman.network.toJson
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.resume
import kotlin.random.Random

class DownloadAppWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    init {
        logi("creating work manager ....")
    }

    private var finished = false
    private var progress = 0L
    private val doneData = workDataOf("done" to true)
    private val databaseHelper = getValueSafeOf(_downloadedRepository)
    private val settingRepository = getValueSafeOf(_optionRepository)
    private val rootedRepository = getValueSafeOf(_rootRepository)

    override suspend fun doWork() = withContext(Dispatchers.IO) {
        callFunction(this)
    }

    private suspend fun callFunction(scope: CoroutineScope): Result =
        suspendCancellableCoroutine { task ->
            scope.launch {
                val autoInstaller = settingRepository?.autoInstallerSync()
                val fileString = inputData.getString("file")
                val file = fileString?.toAny(FileDownload::class.java)

                logi("file is ----> $file")
                val packageName = file?.packageName

                val downloadIsRunForApp = databaseHelper?.checkIsRun(packageName) ?: false
                logi("download is run for ${file?.name} -> $downloadIsRunForApp")

                if (downloadIsRunForApp) {
                    val downloadIdSaved = databaseHelper?.getDownloadId(packageName)
                    databaseHelper?.markIsRun(this, packageName, downloadIdSaved)
                    task.observingDownload(this, downloadIdSaved, file, autoInstaller)
                } else {
                    val downloadId =
                        DownloadUtils.startDownload(file, !(autoInstaller?.value ?: true))
                    databaseHelper?.markIsRun(this, packageName, downloadId)
                    task.observingDownload(this, downloadId, file, autoInstaller)
                }
            }
        }

    private suspend fun CancellableContinuation<Result>.observingDownload(
        scope: CoroutineScope,
        downloadId: Long?,
        file: FileDownload?,
        autoInstaller: SettingData?
    ) {

        val progressPreparing = workDataOf("status_string" to "Preparing")
        setProgress(progressPreparing)

        val task = this
        DownloadUtils.setDownloadListener(downloadId, object : DownloadUtils.DownloadListener {
            override suspend fun onSuccess(cursor: Cursor, status: Download.Status) {
                delay(1000)
                if (autoInstaller?.value == true) {
                    val notificationId = Random.nextInt(1, 99)
                    val notificationCompleteId = Random.nextInt(100, 199)

                    val notificationInstallerBuilder = NotificationCompat.Builder(
                        this@DownloadAppWorker.context,
                        StringValues.notificationInstallerId
                    ).apply {
                        setContentTitle("Installing ${file?.name}")
                        setContentText("Installing in progress")
                        setSmallIcon(R.drawable.ic_fluent_apps_add_in_24_regular)
                        priority = NotificationCompat.PRIORITY_MAX
                    }

                    val notificationManagerCompat =
                        NotificationManagerCompat.from(this@DownloadAppWorker.context)

                    notificationManagerCompat.run {
                        notificationInstallerBuilder.setProgress(0, 100, true)
                        notify(notificationId, notificationInstallerBuilder.build())
                    }

                    val statusString = "Installing..."
                    val statusJson = status.toJson()

                    val progressData = workDataOf(
                        "status_string" to statusString,
                        "status" to statusJson
                    )

                    setProgress(progressData)
                    databaseHelper?.markIsComplete(scope, file?.packageName, downloadId)

                    delay(1000)
                    val currentApp = databaseHelper?.getCurrentApp(file?.packageName)
                    val dir =
                        this@DownloadAppWorker.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    val fileDownload = File(dir, "${currentApp?.fileName}.apk")
                    val result = rootedRepository?.installApk(fileDownload.absolutePath, file?.name)
                    logi("result is ---> ${result?.toJson()}")

                    val notificationCompleteBuilder = NotificationCompat.Builder(
                        this@DownloadAppWorker.context,
                        StringValues.notificationInstallerCompleteId
                    ).apply {
                        setContentTitle("${file?.name}")
                        color = if (result?.success == true) {
                            setContentText("Install complete")
                            Color.GREEN
                        } else {
                            setContentText("Install failed because ${result?.message}")
                            Color.RED
                        }
                        setSmallIcon(R.drawable.ic_fluent_apps_add_in_24_regular)
                        priority = NotificationCompat.PRIORITY_MAX
                    }

                    notificationManagerCompat.run {
                        cancel(notificationId)
                        notify(notificationCompleteId, notificationCompleteBuilder.build())
                    }

                } else {
                    val statusJson = status.toJson()
                    val progressData = workDataOf(
                        "status" to statusJson
                    )

                    setProgress(progressData)
                    databaseHelper?.markIsComplete(scope, file?.packageName, downloadId)
                }

                progress = 100
                finished = true

                delay(3000)
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
                databaseHelper?.removeApp(scope, file?.packageName)

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

                databaseHelper?.markIsComplete(scope, file?.packageName, downloadId)
                databaseHelper?.removeApp(scope, file?.packageName)

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