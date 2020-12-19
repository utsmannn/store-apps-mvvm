/*
 * Created by Muhammad Utsman on 19/12/20 11:52 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utsman.data.model.dto.entity.RecentQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentQueryDao {

    @Query("select * from recent_query order by millis desc")
    suspend fun recentQueries(): List<RecentQueryEntity>

    @Query("select * from recent_query order by millis desc")
    fun recentQueriesFlow(): Flow<List<RecentQueryEntity>>

    @Insert
    suspend fun insertQuery(vararg query: RecentQueryEntity)

    @Delete
    suspend fun deleteQuery(query: RecentQueryEntity?)
}