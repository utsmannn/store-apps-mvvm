/*
 * Created by Muhammad Utsman on 20/12/20 4:29 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.database

import com.utsman.data.model.dto.entity.ErrorLogInstallerEntity
import kotlinx.coroutines.flow.Flow

interface ErrorLogInstallerRepository {
    suspend fun getErrorLog(): Flow<List<ErrorLogInstallerEntity>>
    suspend fun insertError(errorLogInstallerEntity: ErrorLogInstallerEntity)
    suspend fun removeAll()
}