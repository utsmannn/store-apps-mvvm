/*
 * Created by Muhammad Utsman on 28/11/20 5:01 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.di

import com.utsman.data.repository.meta.MetaRepository
import com.utsman.detail.domain.DetailUseCase
import com.utsman.detail.viewmodel.DetailViewModel

fun provideDetailUseCase(metaRepository: MetaRepository): DetailUseCase {
    return DetailUseCase(metaRepository)
}

fun provideDetailViewModel(detailUseCase: DetailUseCase): DetailViewModel {
    return DetailViewModel(detailUseCase)
}