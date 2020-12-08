/*
 * Created by Muhammad Utsman on 4/12/20 11:41 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.worker

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.FetchObserver
import com.tonyodev.fetch2core.Reason
import com.utsman.abstraction.ext.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DownloadAppWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        val IDLE = 0
        val START = 1
        val PROGRESS = 2
        val CANCELED = 3
        val WAITING = 4
        val COMPLETE = 5
    }

    private val downloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val filePath by lazy {
        File(Environment.getExternalStorageState(), "/apks")
    }

    private val completeStateListener: MutableStateFlow<Int> = MutableStateFlow(IDLE)

    override suspend fun doWork(): Result {

        return try {
            /*val task = downloadTask(this@DownloadAppWorker, GlobalScope)
            //val aa = task

            //logi("url is --> $url")
            logi("taks -> $task")

            setProgress(workDataOf("lah" to task))
            delay(1000)
            logi("result is success .............")
            delay(10000)
            Result.success()*/
            //downloadTask(this@DownloadAppWorker, GlobalScope)
            //Result.success()
            startDownload()
        } catch (e: Throwable) {
            Result.failure()
        }
    }

    private suspend fun startDownload() = suspendCancellableCoroutine<Result> { task ->
        val url = inputData.getString("file_url")
        val name = inputData.getString("name")
        val fileName = inputData.getString("file_name")
        val doneData = workDataOf("done" to true)

        val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val filePath = "/$fileName.apk"
        val fileStringPath = "$downloadPath/$filePath"

        val configuration = FetchConfiguration.Builder(context)
            .setDownloadConcurrentLimit(1)
            .setNotificationManager(object : DefaultFetchNotificationManager(context) {
                override fun getSubtitleText(
                    context: Context,
                    downloadNotification: DownloadNotification
                ): String {
                    return name ?: ""
                }

                override fun getFetchInstanceForNamespace(namespace: String): Fetch {
                    return Fetch.getDefaultInstance()
                }

            })
            .createDownloadFileOnEnqueue(true)
            .build()

        val fetch = Fetch.getInstance(configuration)

        suspend {
            completeStateListener.collect {
                logi("state is --> $it")
            }
        }

        logi("try check url")
        if (url != null) {
            logi("try request build")
            val request = Request(url, fileStringPath).apply {
                priority = Priority.HIGH
                networkType = NetworkType.ALL

            }

            logi("try enqueue download")
            fetch.enqueue(request)

            fetch.attachFetchObserversForDownload(request.id, object : FetchObserver<Download> {
                override fun onChanged(data: Download, reason: Reason) {
                    val total = data.total
                    val progress = data.progress
                    val status = data.status
                    logi("status === $status")
                    val dataProgress = workDataOf(
                        "total" to total,
                        "progress" to progress
                    )
                    logi("progress is ---> $dataProgress")
                    setProgressAsync(dataProgress)
                    when (status) {
                        Status.COMPLETED -> {
                            if (task.isActive) {
                                task.resume(Result.success(doneData))
                            }
                        }
                        Status.CANCELLED -> {
                            if (task.isActive) {
                                task.resume(Result.failure(doneData))
                            }
                            fetch.delete(request.id)
                        }
                        Status.FAILED -> {
                            val msg = data.error.throwable?.localizedMessage
                            logi("message -> $msg")
                            data.error.throwable?.printStackTrace()
                            if (task.isActive) {
                                task.resume(Result.failure(doneData))
                            }
                            fetch.delete(request.id)
                        }
                        else -> logi("status is -> $status")
                    }
                }

            }).enqueue(request) { error ->
                loge("error -------")
                error.throwable?.printStackTrace()
                if (task.isActive) {
                    task.resume(Result.failure(doneData))
                }
            }
        }
    }
}