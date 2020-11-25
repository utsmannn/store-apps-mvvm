package com.utsman.listing.di

import com.utsman.abstraction.di.Module
import com.utsman.data.repository.InstalledAppsRepository
import com.utsman.data.repository.PagingAppRepository
import com.utsman.listing.domain.InstalledAppUseCase
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import com.utsman.listing.viewmodel.PagingViewModel

fun providePagingViewModel(pagingUseCase: PagingUseCase): Module<PagingViewModel> {
    val data = PagingViewModel(pagingUseCase)
    return Module(data)
}

fun provideInstalledAppViewModel(installedAppUseCase: InstalledAppUseCase): Module<InstalledAppsViewModel> {
    val data = InstalledAppsViewModel(installedAppUseCase)
    return Module(data)
}

fun providePagingUseCase(pagingAppRepository: PagingAppRepository): Module<PagingUseCase> {
    val data = PagingUseCase(pagingAppRepository)
    return Module(data)
}

fun provideInstalledAppUseCase(installedAppsRepository: InstalledAppsRepository): Module<InstalledAppUseCase> {
    val data = InstalledAppUseCase(installedAppsRepository)
    return Module(data)
}