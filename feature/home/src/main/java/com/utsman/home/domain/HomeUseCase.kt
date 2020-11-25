package com.utsman.home.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.utsman.abstraction.dto.ResultState
import com.utsman.abstraction.dto.fetch
import com.utsman.abstraction.dto.stateOf
import com.utsman.data.const.CategoriesApps
import com.utsman.data.model.dto.AppsSealedView.AppsView
import com.utsman.data.model.dto.CategoryView
import com.utsman.data.model.dto.toAppsView
import com.utsman.data.model.dto.toCategoryView
import com.utsman.data.repository.AppsRepository
import com.utsman.data.repository.CategoriesRepository
import com.utsman.data.source.CategoriesPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeUseCase(
    private val appsRepository: AppsRepository,
    private val categoriesRepository: CategoriesRepository
) {
    val randomList = stateOf<List<AppsView>>()
    val categories = stateOf<List<CategoryView>>()

    val pagingCategories = MutableLiveData<PagingData<CategoryView>>()

    suspend fun getRandomApps(scope: CoroutineScope) = scope.launch {
        fetch {
            val response = appsRepository.getRandomApps()
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
            categories.value = it
        }
    }

    suspend fun getPagingCategories(scope: CoroutineScope) = scope.launch {
        Pager(PagingConfig(pageSize = 2)) {
            CategoriesPagingSource(categoriesRepository, appsRepository)
        }.flow
            .cachedIn(this)
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