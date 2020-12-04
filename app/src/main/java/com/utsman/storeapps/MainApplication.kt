/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.utsman.abstraction.base.GlideApp
import com.utsman.network.di.jsonBeautifier
import com.utsman.network.di.moshi
import com.utsman.network.di.provideJsonBeautifier
import com.utsman.network.di.provideMoshi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // provide manual di
        moshi = provideMoshi()
        jsonBeautifier = provideJsonBeautifier()
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