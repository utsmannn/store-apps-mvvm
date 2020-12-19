/*
 * Created by Muhammad Utsman on 13/12/20 3:59 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.dao

import androidx.room.*
import com.utsman.data.model.dto.entity.CurrentDownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentDownloadDao {

    @Query("select * from current_downloads")
    suspend fun currentApps(): List<CurrentDownloadEntity>

    @Query("select * from current_downloads")
    fun currentAppsFlow(): Flow<List<CurrentDownloadEntity?>>

    @Query("select * from current_downloads where package_name = :packageName")
    suspend fun getCurrentApps(packageName: String?): CurrentDownloadEntity?

    @Update
    suspend fun updateCurrentApps(currentDownloadEntity: CurrentDownloadEntity)

    @Insert
    suspend fun insert(vararg currentDownloadEntity: CurrentDownloadEntity)

    @Query("delete from current_downloads where package_name = :packageName")
    fun delete(packageName: String?)

    @Query("delete from current_downloads")
    suspend fun deleteAll()
}