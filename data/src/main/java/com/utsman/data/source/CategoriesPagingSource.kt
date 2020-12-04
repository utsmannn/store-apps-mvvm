/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.source

import androidx.paging.PagingSource
import com.utsman.data.const.StringValues
import com.utsman.data.model.Category
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.data.model.dto.list.CategorySealedView
import com.utsman.data.model.dto.list.CategorySealedView.CategoryView
import com.utsman.data.model.dto.list.toAppsBannerView
import com.utsman.data.model.dto.list.toCategoryBannerView
import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.repository.list.CategoriesRepository
import com.utsman.data.repository.list.InstalledAppsRepository
import java.lang.Exception
import java.net.SocketException
import java.net.SocketTimeoutException

class CategoriesPagingSource(
    private val categoryRepository: CategoriesRepository,
    private val appsRepository: AppsRepository,
    private val installedAppsRepository: InstalledAppsRepository
) : PagingSource<Int, CategorySealedView>() {

    private val page = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategorySealedView> {
        return try {
            val currentPage = params.key ?: page
            val data = categoryRepository.getCategoriesView(currentPage) ?: emptyList()
            val newData = data.apply {
                this.map {
                    when (it) {
                        is CategoryView -> {
                            it.apps.map { ap ->
                                when (ap) {
                                    is AppsSealedView.AppsView ->
                                        installedAppsRepository.checkInstalledApps(ap)
                                    else -> ap
                                }
                            }
                        }
                        else -> it
                    }
                }
            }

            val prevPage = if (currentPage <= 0) null else currentPage - 1
            val nextPage = if (!newData.isNullOrEmpty()) currentPage + 2 else null

            when (currentPage) {
                0 -> {
                    val covidAppsCategory = Category.simple {
                        this.name = StringValues.covidTitle
                        this.query = "covid"
                        this.image = StringValues.covidBannerUrl
                        this.desc = StringValues.covidDesc
                    }
                    val covidAppsCategoryView = appsRepository.getSearchApps(covidAppsCategory.query, 0, 10)
                        .toCategoryBannerView(covidAppsCategory)
                        ?: CategorySealedView.CategoryBannerView()

                    val topAppsCategoryView = appsRepository.getTopApps()
                        .datalist?.list?.map { app ->
                            app.toAppsBannerView()
                        } ?: emptyList<AppsSealedView>()

                    val topCategoryView = CategoryView.simple {
                        name = "Top downloads"
                        query = null
                        apps = topAppsCategoryView
                    }

                    val sportAppsCategory = Category.simple {
                        this.name = StringValues.sportTitle
                        this.query = "sport"
                        this.image = StringValues.sportBannerUrl
                        this.desc = StringValues.sportDesc
                    }

                    val sportAppsCategoryView = appsRepository.getSearchApps(sportAppsCategory.query, 0, 10)
                        .toCategoryBannerView(sportAppsCategory) ?: CategorySealedView.CategoryBannerView()

                    // push custom category view
                    val reversed = newData.toMutableList().asReversed().apply {
                        add(topCategoryView)
                        add(sportAppsCategoryView)
                        add(covidAppsCategoryView)
                    }.apply {
                        reverse()
                    }.toList()

                    LoadResult.Page(reversed, prevPage, nextPage)
                }
                else -> LoadResult.Page(newData, prevPage, nextPage)
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        } catch (e: SocketTimeoutException) {
            LoadResult.Error(e)
        } catch (e: SocketException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}