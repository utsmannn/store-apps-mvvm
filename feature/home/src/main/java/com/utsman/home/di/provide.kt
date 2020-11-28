/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.di

import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.repository.list.CategoriesRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.home.domain.HomeUseCase
import com.utsman.home.viewmodel.HomeViewModel

fun provideHomeUseCase(
    appsRepository: AppsRepository,
    categoriesRepository: CategoriesRepository,
    installedAppsRepository: InstalledAppsRepository
): HomeUseCase {
    return HomeUseCase(appsRepository, categoriesRepository, installedAppsRepository)
}

fun provideHomeViewModel(homeUseCase: HomeUseCase): HomeViewModel {
    return HomeViewModel(homeUseCase)
}
