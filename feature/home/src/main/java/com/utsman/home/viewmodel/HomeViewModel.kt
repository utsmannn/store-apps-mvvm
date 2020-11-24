package com.utsman.home.viewmodel

import androidx.lifecycle.*
import com.utsman.abstraction.dto.ResultState
import com.utsman.data.model.dto.AppsView
import com.utsman.data.model.dto.CategoryView
import com.utsman.home.domain.HomeUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _randomList = homeUseCase.randomList
    private val _categories = homeUseCase.categories
    val randomList: LiveData<ResultState<List<AppsView>>>
        get() = _randomList.asLiveData(viewModelScope.coroutineContext)

    val categories: LiveData<ResultState<List<CategoryView>>>
        get() = _categories.asLiveData(viewModelScope.coroutineContext)

    fun getRandomApps() = viewModelScope.launch {
        homeUseCase.getRandomApps(this)
    }

    fun getCategories() = viewModelScope.launch {
        homeUseCase.getCategories(this)
    }
}