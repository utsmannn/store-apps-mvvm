package com.utsman.data.repository

import com.utsman.data.model.Datalist

interface PagingRepository {
    suspend fun loadApps(query: String? = null, offset: Int): Datalist
}