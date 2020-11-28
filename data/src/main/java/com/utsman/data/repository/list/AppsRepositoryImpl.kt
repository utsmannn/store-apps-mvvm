/*
 * Created by Muhammad Utsman on 28/11/20 4:02 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.model.response.list.Aptoide
import com.utsman.data.route.Services

class AppsRepositoryImpl(private val service: Services) : AppsRepository {
    override suspend fun getTopApps(): Aptoide {
        return service.topList()
    }

    override suspend fun getSearchApps(query: String, offset: Int): Aptoide {
        return service.searchList(query, offset)
    }
}