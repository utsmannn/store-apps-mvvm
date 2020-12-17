/*
 * Created by Muhammad Utsman on 4/12/20 5:45 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.di

import android.content.Context
import com.utsman.data.repository.download.DownloadRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.detail.domain.DetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DetailModule {

    @Provides
    @Singleton
    fun provideDetailUseCase(
        @ApplicationContext context: Context,
        metaRepository: MetaRepository,
        installedAppsRepository: InstalledAppsRepository,
        downloadRepository: DownloadRepository
    ): DetailUseCase {
        return DetailUseCase(context, metaRepository, installedAppsRepository, downloadRepository)
    }
}