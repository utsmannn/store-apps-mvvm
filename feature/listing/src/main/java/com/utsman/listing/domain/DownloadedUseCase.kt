/*
 * Created by Muhammad Utsman on 13/12/20 2:10 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.domain

import androidx.work.WorkManager
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.model.dto.list.toAppsView
import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.utils.CurrentDownloadHelper
import com.utsman.network.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DownloadedUseCase @Inject constructor(
    private val workManager: WorkManager,
    private val appsRepository: AppsRepository,
    downloadHelper: CurrentDownloadHelper
) {

    val list = downloadHelper.getCurrentAppsFlow()
        .mapNotNull {
            it.map { a ->
                val id = "id_${a.packageName}"
                val name = a.name
                val downloadId = a.downloadId
                val workInfo = workManager.getWorkInfoByIdLiveData(UUID.fromString(a.uuid))

                val appsFoundApiService = appsRepository.getSearchApps(a.packageName, 0)
                logi("apps found --> ${appsFoundApiService.toJson()}")
                val appsFound = appsFoundApiService.datalist?.list?.find { i -> i.`package` == a.packageName }?.toAppsView()
                DownloadedApps(id = id, name = name, downloadId = downloadId, workInfoLiveData = workInfo, appsView = appsFound)
            }
        }
}