package com.utsman.storeapps

import android.app.Application
import com.utsman.abstraction.base.GlideApp
import com.utsman.data.di.*
import com.utsman.home.di.*
import com.utsman.listing.di.pagingUseCase
import com.utsman.listing.di.pagingViewModel
import com.utsman.listing.di.providePagingUseCase
import com.utsman.listing.di.providePagingViewModel
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
        pagingRepository = providePagingRepository(services.data)

        homeUseCase = provideHomeUseCase(appsRepository.data, categoriesRepository.data)
        homeViewModel = provideHomeViewModel(homeUseCase.data)

        pagingUseCase = providePagingUseCase(pagingRepository.data)
        pagingViewModel = providePagingViewModel(pagingUseCase.data)

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