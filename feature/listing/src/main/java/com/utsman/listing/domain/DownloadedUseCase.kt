/*
 * Created by Muhammad Utsman on 13/12/20 2:10 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.domain

import androidx.work.WorkManager
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.utils.CurrentDownloadHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DownloadedUseCase @Inject constructor(private val workManager: WorkManager, private val downloadHelper: CurrentDownloadHelper) {

    val list = downloadHelper.getCurrentAppsFlow()
        .mapNotNull {
            it.map { a ->
                val id = "id_${a.packageName}"
                val name = a.name
                val workInfo = workManager.getWorkInfoByIdLiveData(UUID.fromString(a.uuid))
                DownloadedApps(id = id, name = name, workInfoLiveData = workInfo)
            }
        }
}