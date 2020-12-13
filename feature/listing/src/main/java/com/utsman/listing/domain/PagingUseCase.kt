/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.work.WorkManager
import com.utsman.data.model.dto.entity.toDownloadedApps
import com.utsman.data.model.dto.list.AppsSealedView.AppsView
import com.utsman.data.model.dto.list.toAppsView
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import com.utsman.data.repository.list.PagingAppRepository
import com.utsman.data.source.AppsPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class PagingUseCase @Inject constructor(
    private val pagingAppRepository: PagingAppRepository,
    private val installedAppsRepository: InstalledAppsRepository,
    private val downloadedRepository: DownloadedRepository,
    private val workManager: WorkManager
) {
    val pagingData = MutableLiveData<PagingData<AppsView>>()

    fun searchApps(scope: CoroutineScope, query: String? = null, isSearch: Boolean) = scope.launch {
        Pager(PagingConfig(pageSize = 10)) {
            AppsPagingSource(query, isSearch, pagingAppRepository)
        }.flow
            .cachedIn(GlobalScope)
            .collect {
                val appsViewPaging = it.map { ap ->

                    val entityFound = downloadedRepository.getCurrentApp(ap.`package`)
                    val downloadedApps = entityFound?.toDownloadedApps()
                    ap.toAppsView(downloadedApps)
                }.map { ap ->
                    installedAppsRepository.checkInstalledApps(ap)
                }
                pagingData.postValue(appsViewPaging)
            }
    }

    fun restartState(scope: CoroutineScope) = scope.run {
        pagingData.postValue(PagingData.empty())
    }
}