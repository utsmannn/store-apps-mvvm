/*
 * Created by Muhammad Utsman on 13/12/20 4:27 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.utils

import com.utsman.data.dao.CurrentDownloadDao
import com.utsman.data.model.dto.worker.WorkerAppsMap
import com.utsman.data.model.dto.worker.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class CurrentDownloadHelper @Inject constructor(private val dao: CurrentDownloadDao) {

    fun getCurrentAppsFlow() = dao.currentAppsFlow()

    fun markIsRun(scope: CoroutineScope, packageName: String?, downloadId: Long?) = scope.launch {
        val found = dao.getCurrentApps(packageName)
        if (found != null) {
            found.apply {
                this.isRun = true
                this.downloadId = downloadId
            }
            dao.updateCurrentApps(found)
        }
    }

    fun checkIsRun(packageName: String?) =
        dao.getCurrentApps(packageName)?.isRun == true

    fun getDownloadId(packageName: String?) =
        dao.getCurrentApps(packageName)?.downloadId

    suspend fun saveApp(scope: CoroutineScope, workerAppsMap: WorkerAppsMap) = scope.launch {
        val entity = workerAppsMap.toEntity()
        dao.insert(entity)
    }

    fun removeApp(packageName: String?) = dao.delete(packageName)
}