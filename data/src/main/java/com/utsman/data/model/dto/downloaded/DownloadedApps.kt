/*
 * Created by Muhammad Utsman on 13/12/20 2:53 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.downloaded

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.work.WorkInfo
import com.utsman.data.model.dto.list.AppsSealedView

data class DownloadedApps(
    val id: String,
    val name: String,
    val downloadId: Long?,
    val workInfoLiveData: LiveData<WorkInfo>,
    val appsView: AppsSealedView.AppsView?,
    val isRun: Boolean,
    val appStatus: AppStatus,
    val fileName: String
) {
    companion object {
        fun createDivider(status: AppStatus): DownloadedApps? {
            return when (status) {
                AppStatus.DOWNLOAD_DIVIDER, AppStatus.INSTALLED_DIVIDER -> {
                    val name = if (status == AppStatus.DOWNLOAD_DIVIDER) {
                        "Downloaded APK"
                    } else {
                        "Installed"
                    }
                    DownloadedApps(
                        id = status.name,
                        name = name,
                        downloadId = 0L,
                        workInfoLiveData = liveData { },
                        appsView = null,
                        isRun = false,
                        appStatus = status,
                        fileName = "")
                }
                else -> null
            }
        }
    }
}