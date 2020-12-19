/*
 * Created by Muhammad Utsman on 14/12/20 1:09 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.database

import com.utsman.data.model.dto.entity.CurrentDownloadEntity
import com.utsman.data.model.dto.worker.WorkerAppsMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface DownloadedRepository {
    suspend fun getCurrentAppsSuspendFlow(): Flow<List<CurrentDownloadEntity?>>
    fun getCurrentAppsFlow(): Flow<List<CurrentDownloadEntity?>>
    suspend fun getCurrentApp(packageName: String?): CurrentDownloadEntity?
    suspend fun markIsRun(packageName: String?, downloadId: Long?)
    suspend fun checkIsRun(packageName: String?): Boolean
    suspend fun markIsComplete(packageName: String?, downloadId: Long?)
    suspend fun getDownloadId(packageName: String?): Long?
    suspend fun getUUIDWorkManager(packageName: String?): String?
    suspend fun saveApp(workerAppsMap: WorkerAppsMap)
    suspend fun removeApp(packageName: String?)
    suspend fun removeAll()
}