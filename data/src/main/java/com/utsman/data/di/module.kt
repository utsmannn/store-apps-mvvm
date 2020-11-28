/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.di

import org.koin.dsl.module

val serviceModule = module {
    single {
        provideServices()
    }
}

val dataRepositoryModule = module {
    single {
        provideAppsRepository(get())
    }
    single {
        provideCategoriesRepository(get())
    }
    single {
        providePagingRepository(get())
    }
    single {
        provideInstalledAppsRepository(get(), get())
    }
    single {
        provideMetaRepository(get())
    }
}