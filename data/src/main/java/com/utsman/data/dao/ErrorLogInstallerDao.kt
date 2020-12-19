/*
 * Created by Muhammad Utsman on 20/12/20 4:19 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.utsman.data.model.dto.entity.ErrorLogInstallerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ErrorLogInstallerDao {

    @Query("select * from error_installer order by millis desc")
    fun errorLog(): Flow<List<ErrorLogInstallerEntity>>

    @Insert
    suspend fun insertLog(errorLogInstallerEntity: ErrorLogInstallerEntity)

    @Query("delete from error_installer")
    suspend fun deleteAll()
}