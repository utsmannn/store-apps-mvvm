package com.utsman.home.di

import com.utsman.data.repository.AppsRepository
import com.utsman.data.repository.CategoriesRepository
import com.utsman.data.repository.InstalledAppsRepository
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
