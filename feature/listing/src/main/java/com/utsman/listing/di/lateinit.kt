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

}