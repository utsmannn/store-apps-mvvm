/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.utsman.abstraction.base.GlideApp
import com.utsman.data.di.*
import com.utsman.network.di._jsonBeautifier
import com.utsman.network.di._moshi
import com.utsman.network.di.provideJsonBeautifier
import com.utsman.network.di.provideMoshi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        createNotificationChannel()

        // provide manual di
        _moshi.value = provideMoshi()
        _jsonBeautifier.value = provideJsonBeautifier()
        _context.value = this
        _dataStore.value = provideDataStore(this)
        _downloadManager.value = provideDownloadManager(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        GlideApp.get(this).onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        GlideApp.get(this).onTrimMemory(level)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("store_app", "Download Notification", importance).apply {
                description = "Notification for downloading apk"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}