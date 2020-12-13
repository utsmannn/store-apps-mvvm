/*
 * Created by Muhammad Utsman on 13/12/20 2:53 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.downloaded

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.utsman.data.model.dto.list.AppsSealedView

data class DownloadedApps(
    val id: String,
    val name: String,
    val downloadId: Long?,
    val workInfoLiveData: LiveData<WorkInfo>,
    val appsView: AppsSealedView.AppsView?
)