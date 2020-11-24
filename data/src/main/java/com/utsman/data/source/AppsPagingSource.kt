package com.utsman.data.source

import androidx.paging.PagingSource
import com.utsman.data.model.AppsItem
import com.utsman.data.model.dto.AppsView
import com.utsman.data.model.dto.toAppsView
import com.utsman.data.repository.PagingRepository

class AppsPagingSource(private val query: String, private val pagingRepository: PagingRepository) : PagingSource<Int, AppsItem>() {
    private var page: Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppsItem> {
        return try {
            val currentPage = params.key ?: page
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = currentPage + 1
            val response = pagingRepository.loadApps(query, currentPage)
                .list ?: emptyList()
            LoadResult.Page(response, prevPage, nextPage)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}