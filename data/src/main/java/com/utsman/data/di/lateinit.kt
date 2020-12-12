/*
 * Created by Muhammad Utsman on 10/12/20 6:27 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.di

import android.app.DownloadManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.utsman.data.dao.CurrentDownloadDao
import com.utsman.data.utils.CurrentDownloadHelper
import kotlinx.coroutines.flow.MutableStateFlow

const val storeName = "current_worker"

val _context: MutableStateFlow<Context?> = MutableStateFlow(null)
val _dataStore: MutableStateFlow<DataStore<Preferences>?> = MutableStateFlow(null)
val _downloadManager: MutableStateFlow<DownloadManager?> = MutableStateFlow(null)
val _currentDownloadHelper: MutableStateFlow<CurrentDownloadHelper?> = MutableStateFlow(null)

fun provideDataStore(context: Context) = context.createDataStore(name = storeName)