package com.utsman.data.repository

import com.utsman.data.model.Aptoide

interface AppsRepository {
    suspend fun getTopApps(): Aptoide
    suspend fun getSearchApps(query: String, offset: Int): Aptoide
}