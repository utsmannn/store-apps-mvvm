package com.utsman.data.repository

import com.utsman.data.model.Aptoide
import com.utsman.data.model.Category
import com.utsman.data.route.Services

class CategoriesRepositoryImpl(private val services: Services) : CategoriesRepository {
    override suspend fun getCategory(category: Category): Aptoide {
        return services.searchList(query = category.query)
    }

    override suspend fun getCategories(categories: List<Category>): List<Aptoide> {
        return categories.map { category ->
            services.searchList(query = category.query)
        }
    }
}