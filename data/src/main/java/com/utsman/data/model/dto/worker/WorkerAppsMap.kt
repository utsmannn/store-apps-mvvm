/*
 * Created by Muhammad Utsman on 6/12/20 7:07 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.worker

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkerAppsMap(
    @Json(name = "package_name")
    val packageName: String,
    @Json(name = "uuid")
    val uuid: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "is_run")
    var isRun: Boolean = false,
    @Json(name = "download_id")
    var downloadId: Long? = null,
    @Json(name = "file_name")
    var fileName: String? = null
)