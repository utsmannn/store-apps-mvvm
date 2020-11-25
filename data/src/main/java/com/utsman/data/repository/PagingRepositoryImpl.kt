package com.utsman.data.repository

import com.utsman.data.model.Aptoide
import com.utsman.data.model.Datalist
import com.utsman.data.route.Services

class PagingRepositoryImpl(private val services: Services) : PagingRepository {

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