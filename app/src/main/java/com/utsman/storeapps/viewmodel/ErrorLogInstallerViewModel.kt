/*
 * Created by Muhammad Utsman on 20/12/20 4:58 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.storeapps.domain.ErrorLogInstallerUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ErrorLogInstallerViewModel @ViewModelInject constructor(private val errorLogInstallerUseCase: ErrorLogInstallerUseCase) :
    ViewModel() {

    val errorLog
        get() = errorLogInstallerUseCase.errorLogs
            .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            errorLogInstallerUseCase.getErrorLogs()
        }
    }

    fun clearLog() = viewModelScope.launch {
        errorLogInstallerUseCase.removeAll()
    }

    fun restartState() = errorLogInstallerUseCase.restartState()
}