package com.utsman.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utsman.home.domain.HomeUseCase

class HomeViewModelFactory(
    private val homeUseCase: HomeUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(homeUseCase) as T
        } else {
            throw IllegalAccessException()
        }
    }
}
