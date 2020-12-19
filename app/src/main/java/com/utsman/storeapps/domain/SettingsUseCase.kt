/*
 * Created by Muhammad Utsman on 18/12/20 9:26 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.domain

import com.utsman.data.model.dto.setting.SettingData
import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.setting.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    rootedRepository: RootedRepository,
    private val settingRepository: SettingRepository
) {
    val isRooted = rootedRepository.rooted()
    val autoInstaller: MutableStateFlow<SettingData> = MutableStateFlow(SettingData.autoInstaller(false))
    val maturity: MutableStateFlow<SettingData> = MutableStateFlow(SettingData.maturity(false))

    fun toggleAutoInstaller(scope: CoroutineScope) = scope.launch {
        settingRepository.toggleAutoInstaller()
    }

    fun toggleMaturity(scope: CoroutineScope) = scope.launch {
        settingRepository.toggleMaturity()
    }

    suspend fun getValueAutoInstallerSync() = settingRepository.autoInstallerSync()
    suspend fun getValueMaturitySync() = settingRepository.maturitySync()

    fun getValueSettings(scope: CoroutineScope) = scope.launch {
        settingRepository.autoInstaller().collect {
            autoInstaller.value = it
        }

        settingRepository.maturity().collect {
            maturity.value = it
        }
    }
}