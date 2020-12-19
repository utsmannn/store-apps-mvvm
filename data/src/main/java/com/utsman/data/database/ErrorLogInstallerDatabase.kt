/*
 * Created by Muhammad Utsman on 20/12/20 4:25 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utsman.data.dao.ErrorLogInstallerDao
import com.utsman.data.model.dto.entity.ErrorLogInstallerEntity

@Database(entities = [ErrorLogInstallerEntity::class], version = 1)
abstract class ErrorLogInstallerDatabase : RoomDatabase() {
    abstract fun errorLogInstallerDao(): ErrorLogInstallerDao
}