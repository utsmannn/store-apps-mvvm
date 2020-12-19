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
import androidx.work.WorkManager
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.setting.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow

val _context: MutableStateFlow<Context?> = MutableStateFlow(null)
val _downloadManager: MutableStateFlow<DownloadManager?> = MutableStateFlow(null)
val _workManager: MutableStateFlow<WorkManager?> = MutableStateFlow(null)
val _appsRepository: MutableStateFlow<AppsRepository?> = MutableStateFlow(null)
val _downloadedRepository: MutableStateFlow<DownloadedRepository?> = MutableStateFlow(null)
val _installedAppsRepository: MutableStateFlow<InstalledAppsRepository?> = MutableStateFlow(null)
val _rootRepository: MutableStateFlow<RootedRepository?> = MutableStateFlow(null)
val _settingRepository: MutableStateFlow<SettingRepository?> = MutableStateFlow(null)