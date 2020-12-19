/*
 * Created by Muhammad Utsman on 19/12/20 3:45 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.utsman.data.model.dto.setting.SettingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OptionsRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : OptionsRepository {
    private val keyAutoInstaller = preferencesKey<Boolean>("auto_installer")
    private val keyMaturity = preferencesKey<Boolean>("maturity")

    override suspend fun toggleAutoInstaller() {
        val currentValue = autoInstallerSync().value
        dataStore.edit { preferences ->
            preferences[keyAutoInstaller] = !currentValue
        }
    }

    override suspend fun toggleMaturity() {
        val currentValue = maturitySync().value
        dataStore.edit { preferences ->
            preferences[keyMaturity] = !currentValue
        }
    }

    override suspend fun autoInstaller(): Flow<SettingData> {
        return dataStore.data.map { preferences ->
            preferences[keyAutoInstaller] ?: false
        }.map { SettingData.autoInstaller(it) }
    }

    override suspend fun maturity(): Flow<SettingData> {
        return dataStore.data.map { preferences ->
            preferences[keyMaturity] ?: false
        }.map { SettingData.maturity(it) }
    }

    override suspend fun autoInstallerSync(): SettingData {
        val preferences = dataStore.data.first()
        val value = preferences[keyAutoInstaller] ?: false
        return SettingData.autoInstaller(value)
    }

    override suspend fun maturitySync(): SettingData {
        val preferences = dataStore.data.first()
        val value = preferences[keyMaturity] ?: false
        return SettingData.maturity(value)
    }
}