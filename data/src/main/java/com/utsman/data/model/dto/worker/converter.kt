/*
 * Created by Muhammad Utsman on 13/12/20 4:36 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.worker

import com.utsman.data.model.dto.entity.CurrentDownloadEntity

fun WorkerAppsMap.toEntity(): CurrentDownloadEntity {
    return CurrentDownloadEntity(packageName, uuid, name, isRun, downloadId)
}

fun CurrentDownloadEntity.toAppsMap(): WorkerAppsMap {
    return WorkerAppsMap(packageName, uuid, name, isRun, downloadId)
}