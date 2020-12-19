/*
 * Created by Muhammad Utsman on 19/12/20 11:58 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.database

import kotlinx.coroutines.flow.Flow

interface RecentQueryRepository {
    suspend fun getRecentQuery(): Flow<List<String>>
    suspend fun addQuery(vararg query: String)
    suspend fun removeQuery(query: String)
}