/*
 * Created by Muhammad Utsman on 6/12/20 2:58 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.worker

import androidx.work.WorkInfo

sealed class WorkInfoResult(val workData: WorkInfo? = null, val pnData: String? = null) {
    class Stopped : WorkInfoResult()
    data class Working(val data: WorkInfo, val packageName: String) : WorkInfoResult(data, packageName)
}