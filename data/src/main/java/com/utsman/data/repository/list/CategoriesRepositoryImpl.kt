/*
 * Created by Muhammad Utsman on 28/11/20 4:02 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.const.CategoriesApps
import com.utsman.data.model.response.list.Aptoide
import com.utsman.data.model.Category
import com.utsman.data.model.dto.list.CategorySealedView.CategoryView
import com.utsman.data.model.dto.list.toCategoryView
import com.utsman.data.route.Services

class CategoriesRepositoryImpl(private val services: Services) : CategoriesRepository {
    override suspend fun getCategory(category: Category): Aptoide {
        return services.groupList(query = category.query)
    }

    override suspend fun getCategories(categories: List<Category>): List<Aptoide> {
        return categories.map { category ->
            services.groupList(query = category.query)
        }
    }

    override suspend fun getCategories(page: Int): List<Aptoide>? {
        val categories = CategoriesApps.MockPaging.page(page)
        return categories?.map { category ->
            services.groupList(query = category.query)
        }
    }

    override suspend fun getCategoriesView(page: Int): List<CategoryView>? {
        val categories = CategoriesApps.MockPaging.page(page)
        return categories?.map { category ->
            services.groupList(query = category.query)
        }?.mapIndexed { index, aptoide ->
            aptoide.toCategoryView(categories[index]) ?: CategoryView()
        }
    }
}