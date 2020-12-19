/*
 * Created by Muhammad Utsman on 20/12/20 4:56 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.domain

import com.utsman.data.model.dto.entity.toErrorLog
import com.utsman.data.model.dto.setting.ErrorLog
import com.utsman.data.repository.database.ErrorLogInstallerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ErrorLogInstallerUseCase @Inject constructor(
    private val errorLogInstallerRepository: ErrorLogInstallerRepository,
) {
    val errorLogs: MutableStateFlow<List<ErrorLog>> = MutableStateFlow(emptyList())

    suspend fun getErrorLogs() {
        errorLogInstallerRepository.getErrorLog()
            .map { e -> e.map { l -> l.toErrorLog() } }
            .collect {
                errorLogs.value = it
            }
    }

    suspend fun removeAll() {
        errorLogInstallerRepository.removeAll()
    }

    fun restartState() {
        errorLogs.value = emptyList()
    }
}