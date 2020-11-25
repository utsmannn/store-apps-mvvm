package com.utsman.data.repository

import com.utsman.data.model.Datalist

interface PagingAppRepository {
    suspend fun loadApps(query: String? = null, search: Boolean, offset: Int): Datalist
}