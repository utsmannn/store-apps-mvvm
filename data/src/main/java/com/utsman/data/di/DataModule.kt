/*
 * Created by Muhammad Utsman on 4/12/20 5:06 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.di

import android.app.DownloadManager
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.utsman.data.dao.CurrentDownloadDao
import com.utsman.data.database.CurrentDownloadDatabase
import com.utsman.data.repository.list.*
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.repository.meta.MetaRepositoryImpl
import com.utsman.data.route.Services
import com.utsman.data.utils.CurrentDownloadHelper
import com.utsman.network.Network
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
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun downloadManager(@ApplicationContext context: Context): DownloadManager {
        return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @Provides
    fun provideCurrentDownloadDao(currentDownloadDatabase: CurrentDownloadDatabase): CurrentDownloadDao {
        return currentDownloadDatabase.currentDownloadDao()
    }

    @Provides
    @Singleton
    fun provideCurrentDownloadDatabase(@ApplicationContext context: Context): CurrentDownloadDatabase {
        return Room
            .databaseBuilder(context, CurrentDownloadDatabase::class.java, "download")
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrentDownloadHelper(currentDownloadDao: CurrentDownloadDao): CurrentDownloadHelper {
        return CurrentDownloadHelper(currentDownloadDao)
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
    fun provideInstalledAppsRepository(
        @ApplicationContext context: Context,
        services: Services
    ): InstalledAppsRepository =
        InstalledAppsRepositoryImpl(context, services)

    @Provides
    @Singleton
    fun provideMetaRepository(services: Services): MetaRepository =
        MetaRepositoryImpl(services)
}