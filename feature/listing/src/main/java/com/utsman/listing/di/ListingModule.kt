/*
 * Created by Muhammad Utsman on 4/12/20 5:31 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.di

import android.content.Context
import androidx.work.WorkManager
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.repository.database.RecentQueryRepository
import com.utsman.data.repository.download.DownloadRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.list.PagingAppRepository
import com.utsman.listing.domain.DownloadedUseCase
import com.utsman.listing.domain.InstalledAppUseCase
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.domain.RecentQueryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ListingModule {

    @Provides
    @Singleton
    fun providePagingUseCase(
        pagingAppRepository: PagingAppRepository,
        installedAppsRepository: InstalledAppsRepository,
        downloadedRepository: DownloadedRepository
    ): PagingUseCase {
        return PagingUseCase(
            pagingAppRepository,
            installedAppsRepository,
            downloadedRepository
        )
    }

    @Provides
    @Singleton
    fun provideInstalledAppUseCase(
        installedAppsRepository: InstalledAppsRepository,
        downloadRepository: DownloadRepository
    ): InstalledAppUseCase {
        return InstalledAppUseCase(installedAppsRepository, downloadRepository)
    }

    @Provides
    @Singleton
    fun provideDownloadedUseCase(
        @ApplicationContext context: Context,
        downloadedRepository: DownloadedRepository
    ): DownloadedUseCase {
        return DownloadedUseCase(context, downloadedRepository)
    }

    @Provides
    @Singleton
    fun provideRecentQueryUseCase(recentQueryRepository: RecentQueryRepository): RecentQueryUseCase {
        return RecentQueryUseCase(recentQueryRepository)
    }
}