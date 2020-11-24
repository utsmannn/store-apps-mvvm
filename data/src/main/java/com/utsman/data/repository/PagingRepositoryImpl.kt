package com.utsman.data.repository

import com.utsman.data.model.Datalist
import com.utsman.data.route.Services

class PagingRepositoryImpl(private val services: Services) : PagingRepository {
    override suspend fun loadApps(query: String, page: Int): Datalist {
        return services.searchList(query, page).datalist ?: Datalist()
    }
}