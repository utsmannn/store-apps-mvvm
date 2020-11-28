/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailUseCaseModule = module {
    single { provideDetailUseCase(get()) }
}

val detailViewModelModule = module {
    viewModel { provideDetailViewModel(get()) }
}