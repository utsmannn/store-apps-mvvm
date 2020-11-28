/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listPagingUseCase = module {
    single {
        providePagingUseCase(get(), get())
    }
    single {
        provideInstalledAppUseCase(get())
    }
}

val listInstalledViewModel = module {
    viewModel {
        providePagingViewModel(get())
    }

    viewModel {
        provideInstalledAppViewModel(get())
    }

    viewModel {
        provideSearchPagingViewModel(get())
    }
}