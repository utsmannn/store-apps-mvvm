package com.utsman.data.repository

import com.utsman.data.model.Datalist

interface PagingRepository {
    suspend fun loadApps(query: String, page: Int): Datalist
}