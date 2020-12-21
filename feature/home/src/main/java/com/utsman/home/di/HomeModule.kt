/*
 * Created by Muhammad Utsman on 4/12/20 5:10 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.di

import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.repository.list.CategoriesRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.home.domain.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object HomeModule {

    @Provides
    fun provideHomeUseCase(
        appsRepository: AppsRepository,
        categoriesRepository: CategoriesRepository,
        installedAppsRepository: InstalledAppsRepository
    ): HomeUseCase {
        return HomeUseCase(appsRepository, categoriesRepository, installedAppsRepository)
    }
}