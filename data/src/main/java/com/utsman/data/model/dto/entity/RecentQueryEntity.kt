/*
 * Created by Muhammad Utsman on 19/12/20 11:50 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "recent_query")
data class RecentQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = Random.nextInt(),
    @ColumnInfo(name = "query")
    val query: String,
    @ColumnInfo(name = "millis")
    val millis: Long
)