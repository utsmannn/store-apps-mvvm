/*
 * Created by Muhammad Utsman on 13/12/20 4:12 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utsman.data.dao.CurrentDownloadDao
import com.utsman.data.model.dto.entity.CurrentDownloadEntity

@Database(entities = [CurrentDownloadEntity::class], version = 1)
abstract class CurrentDownloadDatabase : RoomDatabase() {
    abstract fun currentDownloadDao(): CurrentDownloadDao
}