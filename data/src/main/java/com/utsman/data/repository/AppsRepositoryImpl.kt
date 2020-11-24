package com.utsman.data.repository

import com.utsman.data.model.Aptoide
import com.utsman.data.route.Services

class AppsRepositoryImpl(private val service: Services) : AppsRepository {
    override suspend fun getRandomApps(): Aptoide {
        return service.randomList()
    }
}