/*
 * Created by Muhammad Utsman on 4/12/20 5:06 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.di

import android.app.DownloadManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.utsman.data.dao.CurrentDownloadDao
import com.utsman.data.dao.ErrorLogInstallerDao
import com.utsman.data.dao.RecentQueryDao
import com.utsman.data.database.CurrentDownloadDatabase
import com.utsman.data.database.ErrorLogInstallerDatabase
import com.utsman.data.database.RecentQueryDatabase
import com.utsman.data.repository.database.*
import com.utsman.data.repository.download.DownloadRepository
import com.utsman.data.repository.download.DownloadRepositoryImpl
import com.utsman.data.repository.list.*
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.repository.meta.MetaRepositoryImpl
import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.root.RootedRepositoryImplement
import com.utsman.data.repository.setting.OptionsRepository
import com.utsman.data.repository.setting.OptionsRepositoryImpl
import com.utsman.data.route.Services
import com.utsman.network.Network
import com.utsman.network.utils.JsonBeautifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideJsonBeautifier(moshi: Moshi) = JsonBeautifier(moshi)

    @Provides
    @Singleton
    fun provideServices(moshi: Moshi): Services {
        return Network.builder("http://ws75.aptoide.com/", moshi)
            .create(Services::class.java)
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
        return Room.databaseBuilder(context, CurrentDownloadDatabase::class.java, "download")
            .build()
    }

    @Provides
    @Singleton
    fun provideRecentQueryDao(recentQueryDatabase: RecentQueryDatabase): RecentQueryDao {
        return recentQueryDatabase.recentQueryDao()
    }

    @Provides
    @Singleton
    fun provideRecentQueryDatabase(@ApplicationContext context: Context): RecentQueryDatabase {
        return Room.databaseBuilder(context, RecentQueryDatabase::class.java, "queries")
            .build()
    }

    @Provides
    @Singleton
    fun provideRecentQueryRepository(recentQueryDao: RecentQueryDao): RecentQueryRepository {
        return RecentQueryRepositoryImpl(recentQueryDao)
    }

    @Provides
    @Singleton
    fun provideDownloadedRepository(downloadDao: CurrentDownloadDao): DownloadedRepository {
        return DownloadedRepositoryImpl(downloadDao)
    }

    @Provides
    @Singleton
    fun provideDownloadRepository(
        workManager: WorkManager,
        downloadedRepository: DownloadedRepository
    ): DownloadRepository {
        return DownloadRepositoryImpl(workManager, downloadedRepository)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.createDataStore(name = "settings_data")
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): OptionsRepository {
        return OptionsRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideRootedRepository(
        @ApplicationContext context: Context,
        errorLogInstallerRepository: ErrorLogInstallerRepository
    ): RootedRepository {
        return RootedRepositoryImplement(context, errorLogInstallerRepository)
    }

    @Provides
    @Singleton
    fun provideErrorLogInstallerDatabase(@ApplicationContext context: Context): ErrorLogInstallerDatabase {
        return Room.databaseBuilder(
            context,
            ErrorLogInstallerDatabase::class.java,
            "error_log_installer"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideErrorLogInstallerDao(errorLogInstallerDatabase: ErrorLogInstallerDatabase): ErrorLogInstallerDao {
        return errorLogInstallerDatabase.errorLogInstallerDao()
    }

    @Provides
    @Singleton
    fun provideErrorLogInstallerRepository(errorLogInstallerDao: ErrorLogInstallerDao): ErrorLogInstallerRepository {
        return ErrorLogInstallerRepositoryImpl(errorLogInstallerDao)
    }
}