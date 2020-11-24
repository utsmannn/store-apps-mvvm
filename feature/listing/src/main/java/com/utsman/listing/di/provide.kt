package com.utsman.listing.di

import com.utsman.abstraction.di.Module
import com.utsman.data.repository.PagingRepository
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.viewmodel.PagingViewModel

fun providePagingViewModel(pagingUseCase: PagingUseCase): Module<PagingViewModel> {
    val data = PagingViewModel(pagingUseCase)
    return Module(data)
}

fun providePagingUseCase(pagingRepository: PagingRepository): Module<PagingUseCase> {
    val data = PagingUseCase(pagingRepository)
    return Module(data)
}