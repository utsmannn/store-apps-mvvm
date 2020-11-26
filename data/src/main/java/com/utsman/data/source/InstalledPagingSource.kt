package com.utsman.data.source

import androidx.paging.PagingSource
import com.utsman.data.model.dto.AppsSealedView
import com.utsman.data.repository.InstalledAppsRepository
import com.utsman.data.repository.InstalledAppsRepositoryImpl

class InstalledPagingSource(private val installedAppsRepository: InstalledAppsRepository) : PagingSource<Int, AppsSealedView.AppsView>() {
    private val page = 0
    private val perPage = InstalledAppsRepositoryImpl.perPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppsSealedView.AppsView> {
        return try {
            val currentPage = params.key ?: page
            val data = installedAppsRepository.getUpdatedAppsInStore(currentPage)
            val prevPage = if (currentPage <= 0) null else currentPage-perPage
            val nextPage = if (data != null) currentPage+perPage+1 else null

            LoadResult.Page(data ?: emptyList(), prevPage, nextPage)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}