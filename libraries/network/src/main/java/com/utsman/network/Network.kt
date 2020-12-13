/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network

import com.squareup.moshi.Moshi
import com.utsman.abstraction.extensions.debug
import com.utsman.abstraction.extensions.getValueOf
import com.utsman.abstraction.extensions.getValueSafeOf
import com.utsman.network.di._moshi
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

    fun builder(url: String, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttp())
        .build()
}