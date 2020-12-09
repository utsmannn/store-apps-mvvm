/*
 * Created by Muhammad Utsman on 4/12/20 11:41 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.worker

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.FetchObserver
import com.tonyodev.fetch2core.Reason
import com.utsman.abstraction.ext.*
import com.utsman.data.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DownloadAppWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        val ACTION_IDLE = 10
        val ACTION_CANCEL = 20
        val ACTION_PAUSE = 30
    }

    private val _stateAction: MutableStateFlow<Int> = MutableStateFlow(ACTION_IDLE)
    private val stateAction: StateFlow<Int>
        get() = _stateAction

    override suspend fun doWork(): Result {

        return try {
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

        val broadcastResult = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val data = intent?.dataString
                logi("data is  ---> $data")
            }
        }

        val configuration = FetchConfiguration.Builder(context)
            .setNamespace(name)
            .setDownloadConcurrentLimit(1)
            .createDownloadFileOnEnqueue(true)
            .build()

        val fetch = Fetch.getInstance(configuration)


        val intent = Intent(context, Class.forName("com.utsman.storeapps.MainActivity"))
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(context, "store_app")
            .setSmallIcon(R.drawable.ic_fluent_arrow_download_16_filled)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)

        /*suspend {
            stateAction.collect {
                logi("state action is ----> $it")
            }

            _stateAction.collect {
                logi("state action from mutable is ----> $it")
            }
        }*/


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
                    val dataProgress = workDataOf(
                        "total" to total,
                        "progress" to progress
                    )
                    logi("progress is ---> $dataProgress")

                    if (progress < 100) {
                        notificationBuilder.run {
                            setContentTitle(name)
                            setProgress(100, progress, false)
                        }
                        notificationManager.notify(12, notificationBuilder.build())
                    } else {
                        notificationBuilder.run {
                            setContentTitle("$name complete")
                            setProgress(0, 0, false)
                        }
                        notificationManager.notify(12, notificationBuilder.build())
                    }


                    setProgressAsync(dataProgress)
                    when (status) {
                        Status.COMPLETED -> {
                            logi("complete")
                            if (task.isActive) {
                                task.resume(Result.success(doneData))
                            }
                        }
                        Status.CANCELLED -> {
                            if (task.isActive) {
                                task.resume(Result.failure(doneData))
                            }
                            notificationBuilder.run {
                                setContentTitle("$name cancel")
                                setProgress(0, 0, false)
                            }
                            notificationManager.notify(12, notificationBuilder.build())
                            fetch.delete(request.id)
                        }
                        Status.FAILED -> {
                            val msg = data.error.throwable?.localizedMessage
                            logi("message -> $msg")
                            data.error.throwable?.printStackTrace()

                            notificationBuilder.run {
                                setContentTitle("$name failed")
                                setProgress(0, 0, false)
                            }
                            notificationManager.notify(12, notificationBuilder.build())

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