package com.utsman.storeapps

import android.app.Application
import com.utsman.abstraction.base.GlideApp
import com.utsman.data.di.*
import com.utsman.home.di.*
import com.utsman.listing.di.*
import com.utsman.network.di.jsonBeautifier
import com.utsman.network.di.moshi
import com.utsman.network.di.provideJsonBeautifier
import com.utsman.network.di.provideMoshi

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        moshi = provideMoshi()
        jsonBeautifier = provideJsonBeautifier()

        services = provideServices()
        appsRepository = provideAppsRepository(services.data)
        categoriesRepository = provideCategoriesRepository(services.data)
        pagingAppRepository = providePagingRepository(services.data)
        installedAppsRepository = provideInstalledAppsRepository(this, services.data)

        homeUseCase = provideHomeUseCase(appsRepository.data, categoriesRepository.data, installedAppsRepository.data)
        homeViewModel = provideHomeViewModel(homeUseCase.data)

        pagingUseCase = providePagingUseCase(pagingAppRepository.data, installedAppsRepository.data)
        pagingViewModel = providePagingViewModel(pagingUseCase.data)

        installedAppUseCase = provideInstalledAppUseCase(installedAppsRepository.data)
        installedAppViewModel = provideInstalledAppViewModel(installedAppUseCase.data)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        GlideApp.get(this).onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        GlideApp.get(this).onTrimMemory(level)
    }


}