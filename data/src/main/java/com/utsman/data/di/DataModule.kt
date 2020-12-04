/*
 * Created by Muhammad Utsman on 4/12/20 5:06 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.di

import android.content.Context
import androidx.work.WorkManager
import com.utsman.data.repository.list.*
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.repository.meta.MetaRepositoryImpl
import com.utsman.data.route.Services
import com.utsman.network.Network
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideServices(): Services {
        return Network.builder("http://ws75.aptoide.com/")
            .create(Services::class.java)
    }

    @Provides
    fun workManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAppsRepository(services: Services): AppsRepository =
        AppsRepositoryImpl(services)

    @Provides
    @Singleton
    fun provideCategoriesRepository(services: Services): CategoriesRepository =
        CategoriesRepositoryImpl(services)

    @Provides
    @Singleton
    fun providePagingRepository(services: Services): PagingAppRepository =
        PagingAppRepositoryImpl(services)

    @Provides
    @Singleton
    fun provideInstalledAppsRepository(@ApplicationContext context: Context, services: Services) : InstalledAppsRepository =
        InstalledAppsRepositoryImpl(context, services)

    @Provides
    @Singleton
    fun provideMetaRepository(services: Services): MetaRepository =
        MetaRepositoryImpl(services)
}