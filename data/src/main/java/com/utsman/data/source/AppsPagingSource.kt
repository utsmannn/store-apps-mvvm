package com.utsman.data.source

import androidx.paging.PagingSource
import com.utsman.data.model.response.list.AppsItem
import com.utsman.data.repository.PagingAppRepository

class AppsPagingSource(
    private val query: String?,
    private val isSearch: Boolean,
    private val pagingAppRepository: PagingAppRepository
) : PagingSource<Int, AppsItem>() {
    private var offset = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppsItem> {
        return try {
            val currentOffset = params.key ?: offset
            val response = pagingAppRepository.loadApps(query, isSearch, currentOffset)
            val prevOffset = if (currentOffset < 0) 0 else currentOffset - 25
            val mustNext = response.next ?: offset
            val nextOffset = if (mustNext == currentOffset) null else mustNext

            val currentList = response.list ?: emptyList()
            LoadResult.Page(currentList, prevOffset, nextOffset)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}