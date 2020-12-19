/*
 * Created by Muhammad Utsman on 20/12/20 4:21 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "error_installer")
data class ErrorLogInstallerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = Random.nextInt(),
    @ColumnInfo(name = "query")
    val name: String?,
    @ColumnInfo(name = "dir")
    val dir: String,
    @ColumnInfo(name = "reason")
    val reason: String,
    @ColumnInfo(name = "millis")
    val millis: Long
)