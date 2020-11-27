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
}