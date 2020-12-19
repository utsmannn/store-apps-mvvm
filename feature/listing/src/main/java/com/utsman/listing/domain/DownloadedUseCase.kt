/*
 * Created by Muhammad Utsman on 13/12/20 2:10 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.domain

import android.content.Context
import com.utsman.data.model.dto.downloaded.AppStatus
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.model.dto.entity.toDownloadedApps
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.utils.DownloadUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadedUseCase @Inject constructor(
    context: Context,
    private val downloadedRepository: DownloadedRepository
) {

    private val downloadedDivider = DownloadedApps.createDivider(AppStatus.DOWNLOAD_DIVIDER)
    private val installedDivider = DownloadedApps.createDivider(AppStatus.INSTALLED_DIVIDER)

    val list = downloadedRepository.getCurrentAppsFlow()
        .map { entities ->
            entities
                .map { a -> a?.toDownloadedApps() }
                .filter { a ->
                    if (a?.isRun == false) {
                        DownloadUtils.checkAppIsDownloaded(context, a.fileName)
                    } else {
                        true
                    }
                }
                .filter { it?.appsView?.packageName != null }
                .toMutableList()
                .apply {
                    val statusList = this.map { it?.appStatus }
                    if (statusList.contains(AppStatus.DOWNLOADED)) {
                        add(downloadedDivider)
                    }
                    if (statusList.contains(AppStatus.INSTALLED)) {
                        add(installedDivider)
                    }
                }
                .distinctBy { it?.id }
                .sortedBy { a ->
                    a?.appStatus?.ordinal
                }
        }

    suspend fun markIsDone(downloadedApps: DownloadedApps) {
        downloadedRepository.markIsComplete(downloadedApps.appsView?.packageName, downloadedApps.downloadId)
    }
}