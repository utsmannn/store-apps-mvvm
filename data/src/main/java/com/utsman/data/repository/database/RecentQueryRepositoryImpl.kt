/*
 * Created by Muhammad Utsman on 19/12/20 11:59 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.database

import com.utsman.data.dao.RecentQueryDao
import com.utsman.data.model.dto.entity.RecentQueryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentQueryRepositoryImpl @Inject constructor(private val dao: RecentQueryDao) : RecentQueryRepository {
    override suspend fun getRecentQuery(): Flow<List<String>> {
        return dao.recentQueriesFlow().map { f -> f.map { l -> l.query } }
    }

    override suspend fun addQuery(vararg query: String) {
        query.forEach { q ->
            dao.insertQuery(RecentQueryEntity(query = q, millis = System.currentTimeMillis()))
        }
    }

    override suspend fun removeQuery(query: String) {
        val q = dao.recentQueries().find { it.query == query }
        dao.deleteQuery(q)
    }
}