package com.utsman.listing.di

import com.utsman.abstraction.di.Module
import com.utsman.data.repository.InstalledAppsRepository
import com.utsman.data.repository.PagingAppRepository
import com.utsman.listing.domain.InstalledAppUseCase
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import com.utsman.listing.viewmodel.PagingViewModel

fun providePagingViewModel(pagingUseCase: PagingUseCase): PagingViewModel {
    return PagingViewModel(pagingUseCase)
}

fun provideInstalledAppViewModel(installedAppUseCase: InstalledAppUseCase): InstalledAppsViewModel {
    return InstalledAppsViewModel(installedAppUseCase)
}

fun providePagingUseCase(pagingAppRepository: PagingAppRepository, installedAppsRepository: InstalledAppsRepository): PagingUseCase {
    return PagingUseCase(pagingAppRepository, installedAppsRepository)
}

fun provideInstalledAppUseCase(installedAppsRepository: InstalledAppsRepository): InstalledAppUseCase {
    return InstalledAppUseCase(installedAppsRepository)
}