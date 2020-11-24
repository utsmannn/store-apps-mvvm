package com.utsman.home.di

import com.utsman.abstraction.di.Module
import com.utsman.home.domain.HomeUseCase
import com.utsman.home.viewmodel.HomeViewModel

lateinit var homeUseCase: Module<HomeUseCase>
lateinit var homeViewModel: Module<HomeViewModel>
