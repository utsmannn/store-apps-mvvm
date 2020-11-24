package com.utsman.data.repository

import com.utsman.data.model.Datalist
import com.utsman.data.route.Services

class PagingRepositoryImpl(private val services: Services) : PagingRepository {

    override suspend fun loadApps(query: String?, offset: Int): Datalist {
        return if (query != null) {
            services.searchList(query = query, offset = offset).datalist ?: Datalist()
        } else {
            services.randomList(offset = offset).datalist ?: Datalist()
        }
    }
}