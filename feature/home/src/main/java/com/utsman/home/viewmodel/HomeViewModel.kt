package com.utsman.home.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.utsman.abstraction.dto.ResultState
import com.utsman.data.model.dto.AppsSealedView.AppsView
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

    val pagingCategories: LiveData<PagingData<CategoryView>> get() = homeUseCase.pagingCategories

    fun getRandomApps() = viewModelScope.launch {
        homeUseCase.getRandomApps(this)
    }

    fun getCategories() = viewModelScope.launch {
        homeUseCase.getCategories(this)
    }

    fun getPagingCategories() = viewModelScope.launch {
        homeUseCase.getPagingCategories(this)
    }

    fun restartState() = viewModelScope.launch {
        homeUseCase.restartState(this)
    }
}