/*
 * Created by Muhammad Utsman on 4/12/20 5:31 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.di

import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.list.PagingAppRepository
import com.utsman.listing.domain.InstalledAppUseCase
import com.utsman.listing.domain.PagingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ListingModule {

    @Provides
    @Singleton
    fun providePagingUseCase(pagingAppRepository: PagingAppRepository, installedAppsRepository: InstalledAppsRepository): PagingUseCase {
        return PagingUseCase(pagingAppRepository, installedAppsRepository)
    }

    @Provides
    @Singleton
    fun provideInstalledAppUseCase(installedAppsRepository: InstalledAppsRepository): InstalledAppUseCase {
        return InstalledAppUseCase(installedAppsRepository)
    }
}