/*
 * Created by Muhammad Utsman on 18/12/20 9:29 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.di

import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.setting.SettingRepository
import com.utsman.storeapps.domain.SettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSettingUseCase(rootedRepository: RootedRepository, settingRepository: SettingRepository) =
        SettingsUseCase(rootedRepository, settingRepository)
}