package com.utsman.data.repository

import com.utsman.data.model.response.list.Aptoide

interface AppsRepository {
    suspend fun getTopApps(): Aptoide
    suspend fun getSearchApps(query: String, offset: Int): Aptoide
}