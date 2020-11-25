package com.utsman.data.repository

import com.utsman.data.model.Datalist
import com.utsman.data.route.Services

class PagingAppRepositoryImpl(private val services: Services) : PagingAppRepository {

    override suspend fun loadApps(query: String?, search: Boolean, offset: Int): Datalist {
        return if (query != null) {
            if (search) {
                services.searchList(query, offset).datalist ?: Datalist()
            } else {
                services.groupList(query = query, offset = offset).datalist ?: Datalist()
            }
        } else {
            services.topList(offset = offset).datalist ?: Datalist()
        }
    }
}