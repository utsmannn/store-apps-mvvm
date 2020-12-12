/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.utsman.network.utils.JsonBeautifier
import kotlinx.coroutines.flow.MutableStateFlow

val _moshi: MutableStateFlow<Moshi?> = MutableStateFlow(null)
val _jsonBeautifier: MutableStateFlow<JsonBeautifier?> = MutableStateFlow(null)

fun provideMoshi(): Moshi = Moshi
    .Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun provideJsonBeautifier() = JsonBeautifier()