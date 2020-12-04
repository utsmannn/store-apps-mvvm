/*
 * Created by Muhammad Utsman on 4/12/20 5:45 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.di

import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.detail.domain.DetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DetailModule {

    @Provides
    @Singleton
    fun provideDetailUseCase(metaRepository: MetaRepository, installedAppsRepository: InstalledAppsRepository): DetailUseCase {
        return DetailUseCase(metaRepository, installedAppsRepository)
    }
}