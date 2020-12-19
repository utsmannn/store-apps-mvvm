/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.viewmodel

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.interactor.ResultState
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.data.model.dto.list.CategorySealedView
import com.utsman.home.domain.HomeUseCase
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val homeUseCase: HomeUseCase,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val STATE_RV = "state_rv"
    }

    private val _randomList = homeUseCase.randomList
    private val _categories = homeUseCase.categories

    private var stateRecyclerView: Parcelable?
        get() = state.get(STATE_RV)
        set(value) {
            state.set(STATE_RV, value)
        }

    val randomList: LiveData<ResultState<List<AppsView>>>
        get() = _randomList.asLiveData(viewModelScope.coroutineContext)

    val categories: LiveData<ResultState<List<CategorySealedView>>>
        get() = _categories.asLiveData(viewModelScope.coroutineContext)

    val pagingCategories: LiveData<PagingData<CategorySealedView>> get() = homeUseCase.pagingCategories

    fun getRandomApps() = viewModelScope.launch {
        homeUseCase.getRandomApps()
    }

    fun getCategories() = viewModelScope.launch {
        homeUseCase.getCategories()
    }

    fun getPagingCategories() = viewModelScope.launch {
        if (pagingCategories.value == null) {
            homeUseCase.getPagingCategories()
        }
    }

    fun onResumeRecyclerView(recyclerView: RecyclerView?) {
        if (stateRecyclerView != null) {
            recyclerView?.layoutManager?.onRestoreInstanceState(stateRecyclerView)
        }
    }

    fun onPausedRecyclerView(recyclerView: RecyclerView?) {
        stateRecyclerView = recyclerView?.layoutManager?.onSaveInstanceState()
    }

    fun restartState() = viewModelScope.launch {
        homeUseCase.restartState()
    }
}