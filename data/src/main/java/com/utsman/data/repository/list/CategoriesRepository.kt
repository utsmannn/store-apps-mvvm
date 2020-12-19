/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.model.response.list.Aptoide
import com.utsman.data.model.dto.list.Category
import com.utsman.data.model.dto.list.CategorySealedView

interface CategoriesRepository {
    suspend fun getCategory(category: Category) : Aptoide
    suspend fun getCategories(categories: List<Category>) : List<Aptoide>
    suspend fun getCategories(page: Int): List<Aptoide>?
    suspend fun getCategoriesView(page: Int): List<CategorySealedView>?
}