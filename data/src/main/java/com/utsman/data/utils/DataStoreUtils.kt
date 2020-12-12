/*
 * Created by Muhammad Utsman on 12/12/20 10:47 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.utils

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.utsman.abstraction.extensions.getValueOf
import com.utsman.data.di._dataStore
import com.utsman.data.model.dto.worker.WorkerAppsMap
import com.utsman.network.toAnyList
import com.utsman.network.toJson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

object DataStoreUtils {

    private fun dataStore() = getValueOf(_dataStore)

    private val key = preferencesKey<String>("apps")
    val currentApps = dataStore()?.data
        ?.mapNotNull { pref ->
            pref[key]?.toAnyList(WorkerAppsMap::class.java)
        }

    suspend fun currentAppsValue(): List<WorkerAppsMap> {
        val list = currentApps?.first() ?: emptyList()
        list.distinct()
        return list
    }

    suspend fun markIsRun(packageName: String?, downloadId: Long?) {
        val found = currentAppsValue().find { a -> a.packageName == packageName }
        if (found != null) {
            removeApp(packageName)
            found.apply {
                this.isRun = true
                this.downloadId = downloadId
            }
            saveApp(found)
        }
    }

    suspend fun checkIsRun(packageName: String?) =
        currentAppsValue().find { a -> a.packageName == packageName }?.isRun == true

    suspend fun getDownloadId(packageName: String?) =
        currentAppsValue().find { a -> a.packageName == packageName && a.downloadId != null }?.downloadId

    suspend fun saveApp(workerAppsMap: WorkerAppsMap) = run {
        dataStore()?.edit { pref ->
            val valueResult = pref[key]
            val listResult = valueResult?.toAnyList(WorkerAppsMap::class.java)?.toMutableList()
                ?: mutableListOf()
            listResult.distinct()
            if (!listResult.contains(workerAppsMap)) {
                listResult.add(workerAppsMap)
            }
            val valueReturn = listResult.toJson()
            pref[key] = valueReturn
        }
    }

    suspend fun removeApp(packageName: String?) = run {
        dataStore()?.edit { pref ->
            val valueResult = pref[key]
            val listResult = valueResult?.toAnyList(WorkerAppsMap::class.java)?.toMutableList()
                ?: mutableListOf()
            listResult.distinct()
            val foundItem = listResult.find { a -> a.packageName == packageName }
            if (foundItem != null) {
                listResult.remove(foundItem)
            }
            val valueReturn = listResult.toJson()
            pref[key] = valueReturn
        }
    }
}