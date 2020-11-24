package com.utsman.network.di

import com.squareup.moshi.Moshi
import com.utsman.abstraction.di.Module
import com.utsman.network.utils.JsonBeautifier

fun provideMoshi(): Module<Moshi> {
    val data = Moshi
        .Builder()
        .build()
    return Module(data)
}

fun provideJsonBeautifier() = Module(JsonBeautifier())