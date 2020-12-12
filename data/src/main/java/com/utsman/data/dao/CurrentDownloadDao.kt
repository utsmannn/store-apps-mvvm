/*
 * Created by Muhammad Utsman on 13/12/20 3:59 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.utsman.data.model.dto.entity.CurrentDownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentDownloadDao {

    @Query("select * from current_downloads")
    fun currentApps(): List<CurrentDownloadEntity>

    @Query("select * from current_downloads")
    fun currentAppsFlow(): Flow<List<CurrentDownloadEntity>>

    @Query("select * from current_downloads where package_name = :packageName")
    fun getCurrentApps(packageName: String?): CurrentDownloadEntity?

    @Update
    suspend fun updateCurrentApps(currentDownloadEntity: CurrentDownloadEntity)

    @Insert
    suspend fun insert(vararg currentDownloadEntity: CurrentDownloadEntity)

    @Query("delete from current_downloads where package_name = :packageName")
    fun delete(packageName: String?)
}