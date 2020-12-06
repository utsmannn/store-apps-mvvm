/*
 * Created by Muhammad Utsman on 4/12/20 11:41 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.utsman.abstraction.ext.logi
import kotlinx.coroutines.delay

class DownloadAppWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        return try {
            val url = inputData.getString("file_url")

            val data1 = workDataOf("data" to "satu")
            val data2 = workDataOf("data" to "dua")
            val dataDone = workDataOf("done" to true)

            setProgress(data1)
            delay(10000)
            setProgress(data2)

            logi("url is --> $url")
            delay(1000)
            Result.success(dataDone)
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure()
        }
    }
}