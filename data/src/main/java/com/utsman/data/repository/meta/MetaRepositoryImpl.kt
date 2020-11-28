/*
 * Created by Muhammad Utsman on 28/11/20 4:04 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.meta

import com.utsman.data.model.response.detail.AptoideMeta
import com.utsman.data.model.response.detail.Data
import com.utsman.data.route.Services

class MetaRepositoryImpl(private val services: Services) : MetaRepository {
    override suspend fun getDetail(packageName: String): AptoideMeta? {
        return services.getMeta(packageName)
    }
}