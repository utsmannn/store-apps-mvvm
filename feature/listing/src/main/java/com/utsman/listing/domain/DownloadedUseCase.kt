/*
 * Created by Muhammad Utsman on 13/12/20 2:10 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.domain

import android.content.Context
import com.utsman.data.model.dto.entity.toDownloadedApps
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.utils.DownloadUtils
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class DownloadedUseCase @Inject constructor(
    context: Context,
    downloadedRepository: DownloadedRepository
) {

    val list = downloadedRepository.getCurrentAppsFlow()
        .mapNotNull { entities ->
            entities
                .map { a -> a.toDownloadedApps() }
                .filter { a ->
                    DownloadUtils.checkAppIsDownloaded(context, a.fileName)
                }
                .sortedBy { a ->
                    a.appStatus.ordinal
                }
        }
}