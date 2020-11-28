/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeUseCaseModule = module {
    single {
        provideHomeUseCase(get(), get(), get())
    }
}

val homeViewModelModule = module {
    viewModel {
        provideHomeViewModel(get())
    }
}
