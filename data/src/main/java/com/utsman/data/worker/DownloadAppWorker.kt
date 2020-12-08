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
import com.utsman.abstraction.ext.getUriFromFile
import com.utsman.abstraction.ext.logi
import com.utsman.abstraction.ext.toSumReadable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DownloadAppWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val downloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val filePath by lazy {
        File(Environment.getExternalStorageState(), "/apks")
    }

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
            Result.success()
        } catch (e: Throwable) {
            Result.failure()
        }
    }

    private suspend fun downloadTask(downloadAppWorker: DownloadAppWorker, coroutineScope: CoroutineScope) = suspendCoroutine<Result> {
        var finished = false
        var progress = 0L
        val url = inputData.getString("file_url")
        val name = inputData.getString("name")
        val fileName = inputData.getString("file_name")

        logi("try make path")
        if (!filePath.exists()) filePath.mkdir()

        //val fileDownloaded = File(filePath, "$fileName.apk")
        logi("try get uri file")
        //val uri = Uri.fromFile(fileDownloaded)

        val downloadRequest = DownloadManager.Request(Uri.parse(url)).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "$fileName.apk")
            //setAllowedOverMetered(true)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_MOBILE)
            setTitle("Downloading $name")
        }

        val downloadId = downloadManager.enqueue(downloadRequest)
        //it.resume(downloadId)

        if (!finished) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_FAILED -> {
                        logi("failed ----")
                        finished = true
                        val doneData = workDataOf("done" to true)
                        it.resume(Result.failure())
                        //downloadAppWorker.setProgress(doneData)
                        //coroutineScope.cancel()
                    }
                    DownloadManager.STATUS_PAUSED -> {
                        logi("paused ----")
                    }
                    DownloadManager.STATUS_PENDING -> {
                        logi("pending ----")
                    }
                    DownloadManager.STATUS_RUNNING -> {
                        logi("running ----")
                        val size =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                       /* if (size > 0) {

                        } else {
                            logi("size is nol")
                        }*/

                        val downloadedSoFar =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        progress = (downloadedSoFar * 100L) / size
                        logi("size: ${size.toSumReadable()} | downloaded: ${downloadedSoFar.toSumReadable()} | $progress %")
                        val progressData = workDataOf("data" to progress)
                        setProgressAsync(progressData)
                        //downloadAppWorker.setProgress(progressData)
                        //it.resume(Result.success())
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        logi("success ----")
                        progress = 100
                        finished = true
                        val doneData = workDataOf("done" to true)
                        //downloadAppWorker.setProgress(doneData)
                        //coroutineScope.cancel()
                        it.resume(Result.success())
                    }
                }
            }

            //it.resume(Result.failure())
        }
    }
}