package com.utsman.data.source

import androidx.paging.PagingSource
import com.utsman.data.model.AppsItem
import com.utsman.data.repository.PagingRepository

class AppsPagingSource(private val query: String? = null, private val pagingRepository: PagingRepository) : PagingSource<Int, AppsItem>() {
    private var offset = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppsItem> {
        return try {
            val currentOffset = params.key ?: offset
            val response = pagingRepository.loadApps(query, currentOffset)
            val prevOffset = if (currentOffset < 0) 0 else currentOffset - 25
            val nextOffset = response.next ?: offset

            val currentList = response.list ?: emptyList()
            LoadResult.Page(currentList, prevOffset, nextOffset)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}