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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.*
import javax.inject.Inject

class CurrentWorkerPreferences @Inject constructor(context: Context) {
    private val name = "current_worker"
    private val keyPackage = preferencesKey<String>("package")
    private val keyUUID = preferencesKey<String>("uuid")

    private val dataStore = context.createDataStore(name = name)

    val currentUUID = dataStore.data
        .mapNotNull { pref ->
            pref[keyUUID]
        }

    val currentPackage = dataStore.data
        .map { pref ->
            pref[keyPackage] ?: ""
        }

    suspend fun saveUUID(uuid: UUID) = run {
        dataStore.edit { pref ->
            pref[keyUUID] = uuid.toString()
        }
    }

    suspend fun saveCurrentPackage(packageName: String) = run {
        dataStore.edit { pref ->
            pref[keyPackage] = packageName
        }
    }

    suspend fun clearCurrentPackage() = run {
        dataStore.edit { pref ->
            pref.remove(keyPackage)
            pref.remove(keyUUID)
        }
    }

    suspend fun testYa() = run {
        logi("anjaaaayyyy")
    }
}