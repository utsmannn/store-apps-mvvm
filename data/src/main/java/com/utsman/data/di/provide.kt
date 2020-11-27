package com.utsman.data.di

import android.content.Context
import com.utsman.abstraction.di.Module
import com.utsman.data.repository.*
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