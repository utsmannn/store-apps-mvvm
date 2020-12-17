/*
 * Created by Muhammad Utsman on 12/12/20 10:47 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.utsman.abstraction.extensions.*
import com.utsman.data.di._context
import com.utsman.data.di._downloadManager
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.network.toAny
import com.utsman.network.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.io.File

object DownloadUtils {

    private fun getContext() =  getValueOf(_context)
    private fun downloadManager() = getValueOf(_downloadManager)

    fun startDownload(fileDownload: FileDownload?): Long? {
        val downloadRequest = DownloadManager.Request(Uri.parse(fileDownload?.url)).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, "${fileDownload?.fileName}.apk")
            setAllowedOverMetered(true)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_MOBILE)
            setTitle("Downloading ${fileDownload?.name}")
        }

        return downloadManager().enqueue(downloadRequest)
    }

    suspend fun setDownloadListener(downloadId: Long?, listener: DownloadListener) {
        val ticker = ticker(1000)
        ticker.consumeAsFlow().collect {
            ticker.observingCursor(downloadId, listener)
        }
    }

    private fun getCursor(downloadId: Long?): Cursor? {
        return if (downloadId != null) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            downloadManager().query(query)
        } else {
            null
        }
    }

    fun cancel(scope: CoroutineScope, downloadId: Long?) = scope.launch {
        if (downloadId != null) {
            downloadManager().remove(downloadId)
        }
    }

    fun checkAppIsDownloaded(context: Context, fileName: String): Boolean {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "$fileName.apk")
        return file.exists()
    }

    fun whenAppIsDownloaded(context: Context, fileName: String, action: () -> Unit) {
        if (checkAppIsDownloaded(context, fileName)) {
            action.invoke()
        }
    }

    fun checkAppIsInstalled(packageName: String): Boolean {
        val packageManager = getContext().packageManager
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun openDownloadFile(activity: Activity? = null, fragment: Fragment? = null, fileName: String, requestCode: Int = 30) {
        val context = activity ?: fragment?.requireContext()!!

        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "$fileName.apk")
        logi("file uri -> ${file.absolutePath}")
        logi("file is exist --> ${file.exists()}")

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, "com.utsman.storeapps.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }

        val type = "application/vnd.android.package-archive"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, type)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {

            // val startForResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            //    if (result.resultCode == Activity.RESULT_OK) {
            //        val intent = result.intent
            //        // Handle the Intent
            //    }
            //}

            //val startResult = (activity as AppCompatActivity?)?.register

            logi("start activity...")
            activity?.startActivityForResult(intent, requestCode)
            fragment?.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            loge(e.message)
        }
    }

    private suspend fun ReceiveChannel<Unit>.observingCursor(downloadId: Long?, listener: DownloadListener) {
        val cursor = getCursor(downloadId)
        if (cursor != null && cursor.moveToFirst()) {
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    listener.onSuccess(cursor)
                    cancel()
                }
                DownloadManager.STATUS_RUNNING -> {
                    val total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val soFar = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val progress = (soFar * 100L) / total

                    val fileSizeObserver = FileSizeObserver.simple {
                        this.total = total
                        this.soFar = soFar
                        this.progress = progress
                    }

                    if (total > 1 && total == soFar) {
                        listener.onSuccess(cursor)
                        cancel()
                    } else {
                        listener.onRunning(cursor, fileSizeObserver)
                    }

                }
                DownloadManager.STATUS_PAUSED -> {
                    listener.onPaused(cursor)
                }
                DownloadManager.STATUS_PENDING -> {
                    listener.onPending(cursor)
                }
                DownloadManager.STATUS_FAILED -> {
                    listener.onFailed(cursor)
                    cancel()
                }
            }
        } else {
            listener.onCancel()
            cancel()
        }
    }

    interface DownloadListener {
        suspend fun onSuccess(cursor: Cursor)
        suspend fun onRunning(cursor: Cursor, fileSizeObserver: FileSizeObserver)
        suspend fun onPaused(cursor: Cursor)
        suspend fun onPending(cursor: Cursor)
        suspend fun onFailed(cursor: Cursor)
        suspend fun onCancel()
    }

    data class FileSizeObserver(
        var total: Long = 0L,
        var soFar: Long = 0L,
        var progress: Long = 0,
        var sizeReadable: FileSizeReadable = FileSizeReadable()
    ) {

        companion object {
            fun simple(sizeObserver: FileSizeObserver.() -> Unit) = FileSizeObserver()
                .apply(sizeObserver)
                .apply {
                    val totalRead = this.total.toBytesReadable()
                    val soFarRead = this.soFar.toBytesReadable()
                    val progress = "$progress %"
                    sizeReadable = FileSizeReadable(total = totalRead, soFar = soFarRead, progress = progress)
                }

            fun convertFromString(value: String?): FileSizeObserver? {
                return value?.toAny(FileSizeObserver::class.java)
            }
        }

        data class FileSizeReadable(
            var total: String? = "",
            var soFar: String? = "",
            var progress: String? = ""
        )

        fun convertToString(): String {
            return this.toJson()
        }
    }
}