/*
 * Created by Muhammad Utsman on 20/12/20 4:30 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.database

import com.utsman.data.dao.ErrorLogInstallerDao
import com.utsman.data.model.dto.entity.ErrorLogInstallerEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ErrorLogInstallerRepositoryImpl @Inject constructor(private val logInstallerDao: ErrorLogInstallerDao) :
    ErrorLogInstallerRepository {
    override suspend fun getErrorLog(): Flow<List<ErrorLogInstallerEntity>> {
        return logInstallerDao.errorLog()
    }

    override suspend fun insertError(errorLogInstallerEntity: ErrorLogInstallerEntity) {
        logInstallerDao.insertLog(errorLogInstallerEntity)
    }

    override suspend fun removeAll() {
        logInstallerDao.deleteAll()
    }
}