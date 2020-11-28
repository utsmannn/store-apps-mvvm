/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.utsman.abstraction.base.GlideApp
import com.utsman.data.di.*
import com.utsman.detail.di.detailUseCaseModule
import com.utsman.detail.di.detailViewModelModule
import com.utsman.home.di.*
import com.utsman.listing.di.*
import com.utsman.network.di.jsonBeautifier
import com.utsman.network.di.moshi
import com.utsman.network.di.provideJsonBeautifier
import com.utsman.network.di.provideMoshi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        moshi = provideMoshi()
        jsonBeautifier = provideJsonBeautifier()

        startKoin {
            androidContext(this@MainApplication)
            modules(serviceModule, dataRepositoryModule)
            modules(homeUseCaseModule, homeViewModelModule)
            modules(listPagingUseCase, listInstalledViewModel)
            modules(detailUseCaseModule, detailViewModelModule)
        }
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