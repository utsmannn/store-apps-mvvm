package com.utsman.data.source

import androidx.paging.PagingSource
import com.utsman.data.model.dto.AppsSealedView
import com.utsman.data.model.dto.CategoryView
import com.utsman.data.model.dto.toAppsBannerView
import com.utsman.data.model.dto.toAppsView
import com.utsman.data.repository.AppsRepository
import com.utsman.data.repository.CategoriesRepository

class CategoriesPagingSource(
    private val categoryRepository: CategoriesRepository,
    private val appsRepository: AppsRepository
) : PagingSource<Int, CategoryView>() {

    private val page = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryView> {
        return try {
            val currentPage = params.key ?: page
            val data = categoryRepository.getCategoriesView(currentPage) ?: emptyList()

            val prevPage = if (currentPage <= 0) null else currentPage - 1
            val nextPage = if (!data.isNullOrEmpty()) currentPage + 2 else null

            if (currentPage == 0) {
                // if current page == 0, add random apps category
                val randomAppsCategoryView = appsRepository.getRandomApps()
                    .datalist?.list?.map { app ->
                        app.toAppsBannerView()
                    } ?: emptyList<AppsSealedView>()

                val categoryView = CategoryView.simple {
                    name = "Top downloads"
                    query = null
                    apps = randomAppsCategoryView
                }

                // push random apps category in top of list
                val reversed = data.toMutableList().asReversed().apply {
                    add(categoryView)
                }.apply {
                    reverse()
                }.toList()

                LoadResult.Page(reversed, prevPage, nextPage)
            } else {
                LoadResult.Page(data, prevPage, nextPage)
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}