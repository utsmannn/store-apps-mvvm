/*
 * Created by Muhammad Utsman on 18/12/20 9:29 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.di

import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.repository.database.ErrorLogInstallerRepository
import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.setting.OptionsRepository
import com.utsman.storeapps.domain.ErrorLogInstallerUseCase
import com.utsman.storeapps.domain.OptionsUseCase
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
    fun provideSettingUseCase(
        rootedRepository: RootedRepository,
        optionsRepository: OptionsRepository,
        downloadedRepository: DownloadedRepository
    ) =
        OptionsUseCase(rootedRepository, optionsRepository, downloadedRepository)

    @Provides
    @Singleton
    fun provideErrorLogInstallerUseCase(
        errorLogInstallerRepository: ErrorLogInstallerRepository
    ) = ErrorLogInstallerUseCase(errorLogInstallerRepository)
}