/*
 * Created by Muhammad Utsman on 14/12/20 1:13 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.database

import com.utsman.abstraction.extensions.logi
import com.utsman.data.dao.CurrentDownloadDao
import com.utsman.data.model.dto.entity.CurrentDownloadEntity
import com.utsman.data.model.dto.worker.WorkerAppsMap
import com.utsman.data.model.dto.worker.toEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadedRepositoryImpl @Inject constructor(private val dao: CurrentDownloadDao) : DownloadedRepository {
    override suspend fun getCurrentAppsSuspendFlow(): Flow<List<CurrentDownloadEntity>> {
        return dao.currentAppsFlow()
    }

    override fun getCurrentAppsFlow(): Flow<List<CurrentDownloadEntity>> {
        return dao.currentAppsFlow()
    }

    override fun getCurrentApp(packageName: String?): CurrentDownloadEntity? {
        return dao.getCurrentApps(packageName)
    }

    override suspend fun markIsRun(
        scope: CoroutineScope,
        packageName: String?,
        downloadId: Long?
    ): Job {
        return scope.launch {
            val found = dao.getCurrentApps(packageName)
            if (found != null) {
                found.apply {
                    this.isRun = true
                    this.downloadId = downloadId
                }
                dao.updateCurrentApps(found)
            }
        }
    }

    override suspend fun checkIsRun(packageName: String?): Boolean {
        return dao.getCurrentApps(packageName)?.isRun == true
    }

    override suspend fun markIsComplete(
        scope: CoroutineScope,
        packageName: String?,
        downloadId: Long?
    ): Job {
        return scope.launch {
            val found = dao.getCurrentApps(packageName)
            logi("mark is complete --> $found")
            if (found != null) {
                found.apply {
                    this.isRun = false
                    this.downloadId = downloadId
                }
                dao.updateCurrentApps(found)
            }
        }
    }

    override suspend fun getDownloadId(packageName: String?): Long? {
        return dao.getCurrentApps(packageName)?.downloadId
    }

    override suspend fun getUUIDWorkManager(packageName: String?): String? {
        return dao.getCurrentApps(packageName)?.uuid
    }

    override suspend fun saveApp(scope: CoroutineScope, workerAppsMap: WorkerAppsMap): Job {
        return scope.launch {
            val entity = workerAppsMap.toEntity()
            val currentList = dao.currentApps().distinctBy { it.packageName }
            if (!currentList.contains(entity)) {
                dao.insert(entity)
            }
        }
    }

    override suspend fun removeApp(scope: CoroutineScope, packageName: String?): Job {
        return scope.launch {
            dao.delete(packageName)
        }
    }
}