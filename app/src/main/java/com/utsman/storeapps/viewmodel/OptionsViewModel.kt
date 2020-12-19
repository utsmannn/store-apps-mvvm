/*
 * Created by Muhammad Utsman on 18/12/20 9:24 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.storeapps.domain.OptionsUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OptionsViewModel @ViewModelInject constructor(private val useCase: OptionsUseCase) : ViewModel() {

    val isRoot get() = useCase.isRooted

    val downloadDirSize get() = useCase.sizeDownload
        .asLiveData(viewModelScope.coroutineContext)

    suspend fun valueAutoInstallerSync() =
        withContext(viewModelScope.coroutineContext) {
            useCase.getValueAutoInstallerSync()
        }

    suspend fun valueMaturitySync() =
        withContext(viewModelScope.coroutineContext) {
            useCase.getValueMaturitySync()
        }

    fun toggleAutoInstaller() = viewModelScope.launch {
        useCase.toggleAutoInstaller(this)
    }

    fun toggleMaturity() = viewModelScope.launch {
        useCase.toggleMaturity(this)
    }

    fun getDownloadSize() = viewModelScope.launch {
        useCase.getSizeDownloadDir()
    }

    suspend fun deleteFiles() = useCase.cleanFiles()

    fun countFile() = useCase.countFile()
}