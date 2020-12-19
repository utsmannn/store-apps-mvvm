/*
 * Created by Muhammad Utsman on 13/12/20 10:55 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.download

import androidx.work.Operation
import com.utsman.data.model.dto.worker.FileDownload
import com.utsman.data.model.dto.worker.WorkInfoResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun requestDownload(file: FileDownload): Flow<Operation.State>
    suspend fun cancelDownload(downloadId: Long?)
    suspend fun observerWorkInfo(packageName: String): Flow<WorkInfoResult>
}