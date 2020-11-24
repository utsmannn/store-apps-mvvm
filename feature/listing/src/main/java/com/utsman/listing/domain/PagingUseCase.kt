package com.utsman.listing.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.utsman.data.model.dto.AppsView
import com.utsman.data.model.dto.toAppsView
import com.utsman.data.repository.PagingRepository
import com.utsman.data.source.AppsPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PagingUseCase(private val pagingRepository: PagingRepository) {
    val pagingData = MutableLiveData<PagingData<AppsView>>()

    fun searchApps(scope: CoroutineScope, query: String) = scope.launch {
        Pager(PagingConfig(pageSize = 10)) {
            AppsPagingSource(query, pagingRepository)
        }.flow
            .cachedIn(this)
            .collect {
                val appsViewPaging = it.mapSync { ap ->
                    ap.toAppsView()
                }
                pagingData.postValue(appsViewPaging)
            }
    }
}