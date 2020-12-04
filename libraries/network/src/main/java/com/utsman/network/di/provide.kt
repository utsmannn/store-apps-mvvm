/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network.di

import com.squareup.moshi.Moshi
import com.utsman.abstraction.dimanual.Module
import com.utsman.network.utils.JsonBeautifier

fun provideMoshi(): Module<Moshi> {
    val data = Moshi
        .Builder()
        .build()
    return Module(data)
}

fun provideJsonBeautifier() = Module(JsonBeautifier())