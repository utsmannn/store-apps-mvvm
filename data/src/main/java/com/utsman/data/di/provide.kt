/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.di

import android.content.Context
import com.utsman.data.repository.list.*
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.repository.meta.MetaRepositoryImpl
import com.utsman.data.route.Services
import com.utsman.network.Network

fun provideServices(): Services {
    return Network.builder("http://ws75.aptoide.com/")
        .create(Services::class.java)
}

fun provideAppsRepository(services: Services): AppsRepository =
    AppsRepositoryImpl(services)

fun provideCategoriesRepository(services: Services): CategoriesRepository =
    CategoriesRepositoryImpl(services)

fun providePagingRepository(services: Services): PagingAppRepository =
    PagingAppRepositoryImpl(services)

fun provideInstalledAppsRepository(context: Context, services: Services) : InstalledAppsRepository =
    InstalledAppsRepositoryImpl(context, services)

fun provideMetaRepository(services: Services): MetaRepository =
    MetaRepositoryImpl(services)