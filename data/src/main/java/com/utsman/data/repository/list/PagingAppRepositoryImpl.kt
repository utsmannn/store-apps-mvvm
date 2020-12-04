/*
 * Created by Muhammad Utsman on 28/11/20 4:02 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.model.response.list.Datalist
import com.utsman.data.route.Services
import javax.inject.Inject

class PagingAppRepositoryImpl @Inject constructor(private val services: Services) : PagingAppRepository {

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