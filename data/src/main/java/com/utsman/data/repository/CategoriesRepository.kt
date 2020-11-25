package com.utsman.data.repository

import com.utsman.data.model.Aptoide
import com.utsman.data.model.Category
import com.utsman.data.model.dto.CategorySealedView

interface CategoriesRepository {
    suspend fun getCategory(category: Category) : Aptoide
    suspend fun getCategories(categories: List<Category>) : List<Aptoide>
    suspend fun getCategories(page: Int): List<Aptoide>?
    suspend fun getCategoriesView(page: Int): List<CategorySealedView>?
}