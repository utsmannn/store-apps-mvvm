/*
 * Created by Muhammad Utsman on 19/12/20 11:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utsman.data.dao.RecentQueryDao
import com.utsman.data.model.dto.entity.RecentQueryEntity

@Database(entities = [RecentQueryEntity::class], version = 1)
abstract class RecentQueryDatabase : RoomDatabase() {
    abstract fun recentQueryDao(): RecentQueryDao
}