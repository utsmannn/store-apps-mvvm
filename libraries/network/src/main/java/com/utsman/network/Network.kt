/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network

import com.utsman.abstraction.di.moduleOf
import com.utsman.abstraction.ext.debug
import com.utsman.network.di.moshi
import com.utsman.network.interceptor.LogInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Network {

    private val loggingInterceptor = HttpLoggingInterceptor(LogInterceptor()).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val moshiModule by moduleOf(moshi)

    private fun okHttp() = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .pingInterval(10L, TimeUnit.SECONDS)
        .readTimeout(10L, TimeUnit.SECONDS)
        .connectTimeout(10L, TimeUnit.SECONDS)
        .apply {
            debug {
                addInterceptor(loggingInterceptor)
            }
        }
        .build()

    fun builder(url: String): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshiModule))
        .client(okHttp())
        .build()
}