/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.home.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.utsman.abstraction.interactor.ResultState
import com.utsman.abstraction.interactor.fetch
import com.utsman.abstraction.interactor.stateOf
import com.utsman.data.const.CategoriesApps
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.data.model.dto.list.CategorySealedView
import com.utsman.data.model.dto.list.CategorySealedView.CategoryView
import com.utsman.data.model.dto.list.toAppsView
import com.utsman.data.model.dto.list.toCategoryView
import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.repository.list.CategoriesRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.source.CategoriesPagingSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class HomeUseCase @Inject constructor(
    private val appsRepository: AppsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val installedAppsRepository: InstalledAppsRepository
) {
    val randomList = stateOf<List<AppsView>>()
    val categories = stateOf<List<CategorySealedView>>()

    val pagingCategories = MutableLiveData<PagingData<CategorySealedView>>()

    suspend fun getRandomApps(scope: CoroutineScope) = scope.launch {
        fetch {
            val response = appsRepository.getTopApps()
            response.datalist?.list?.map { app ->
                app.toAppsView()
            } ?: emptyList()
        }.collect {
            randomList.value = it
        }
    }

    suspend fun getCategories(scope: CoroutineScope) = scope.launch {
        fetch {
            val response = categoriesRepository.getCategories(CategoriesApps.list)
            response.mapIndexed { index, aptoide ->
                aptoide.toCategoryView(CategoriesApps.list[index]) ?: CategoryView()
            }
        }.collect {
            categories.value = it as ResultState<List<CategorySealedView>>
        }
    }

    suspend fun getPagingCategories(scope: CoroutineScope) = scope.launch {
        Pager(PagingConfig(pageSize = 2)) {
            CategoriesPagingSource(categoriesRepository, appsRepository, installedAppsRepository)
        }.flow
            .cachedIn(GlobalScope)
            .collect { pagingData ->
                pagingCategories.postValue(pagingData)
            }
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        randomList.value = ResultState.Idle()
        categories.value = ResultState.Idle()
        pagingCategories.postValue(PagingData.empty())
    }
}