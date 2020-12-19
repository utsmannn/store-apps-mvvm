/*
 * Created by Muhammad Utsman on 19/12/20 3:40 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.setting

import com.utsman.data.model.dto.setting.SettingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun toggleAutoInstaller()
    suspend fun toggleMaturity()
    suspend fun autoInstaller(): Flow<SettingData>
    suspend fun maturity(): Flow<SettingData>
    suspend fun autoInstallerSync(): SettingData
    suspend fun maturitySync(): SettingData
}