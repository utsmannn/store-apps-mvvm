package com.utsman.data.repository

import com.utsman.data.model.Aptoide
import com.utsman.data.model.Category

interface CategoriesRepository {
    suspend fun getCategory(category: Category) : Aptoide
    suspend fun getCategories(categories: List<Category>) : List<Aptoide>
}