/*
 * Created by Muhammad Utsman on 13/12/20 3:54 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_downloads")
data class CurrentDownloadEntity(
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "is_run")
    var isRun: Boolean = false,
    @ColumnInfo(name = "download_id")
    var downloadId: Long? = null,
    @ColumnInfo(name = "file_name")
    var fileName: String? = null
)