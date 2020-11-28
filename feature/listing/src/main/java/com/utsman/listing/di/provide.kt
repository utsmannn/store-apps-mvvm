/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.di

import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.list.PagingAppRepository
import com.utsman.listing.domain.InstalledAppUseCase
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import com.utsman.listing.viewmodel.PagingViewModel
import com.utsman.listing.viewmodel.SearchPagingViewModel

fun providePagingViewModel(pagingUseCase: PagingUseCase): PagingViewModel {
    return PagingViewModel(pagingUseCase)
}

fun provideSearchPagingViewModel(pagingUseCase: PagingUseCase): SearchPagingViewModel {
    return SearchPagingViewModel(pagingUseCase)
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