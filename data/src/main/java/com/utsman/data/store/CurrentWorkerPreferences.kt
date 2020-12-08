/*
 * Created by Muhammad Utsman on 5/12/20 11:59 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.remove
import androidx.datastore.preferences.createDataStore
import com.utsman.abstraction.ext.logi
import com.utsman.data.model.dto.worker.WorkerAppsMap
import com.utsman.network.toAny
import com.utsman.network.toAnyList
import com.utsman.network.toJson
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.*
import javax.inject.Inject

class CurrentWorkerPreferences @Inject constructor(context: Context) {
    private val name = "current_worker"
    private val key = preferencesKey<String>("apps")

    private val dataStore = context.createDataStore(name = name)

    val currentApps = dataStore.data
        .mapNotNull { pref ->
            pref[key]?.toAnyList(WorkerAppsMap::class.java)
        }

    suspend fun saveApp(workerAppsMap: WorkerAppsMap) = run {
        dataStore.edit { pref ->
            val valueResult = pref[key]
            val listResult = valueResult?.toAnyList(WorkerAppsMap::class.java)?.toMutableList() ?: mutableListOf()
            listResult.distinctBy { it.packageName }
            listResult.add(workerAppsMap)
            val valueReturn = listResult.toJson()
            pref[key] = valueReturn
        }
    }

    suspend fun removeApp(packageName: String) = run {
        dataStore.edit { pref ->
            val valueResult = pref[key]
            val listResult = valueResult?.toAnyList(WorkerAppsMap::class.java)?.toMutableList() ?: mutableListOf()
            val foundItem = listResult.find { a -> a.packageName == packageName }
            listResult.remove(foundItem)
            val valueReturn = listResult.toJson()
            pref[key] = valueReturn
        }
    }
}