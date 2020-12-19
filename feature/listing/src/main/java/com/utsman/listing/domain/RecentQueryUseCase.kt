/*
 * Created by Muhammad Utsman on 20/12/20 12:14 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.domain

import com.utsman.abstraction.interactor.stateOf
import com.utsman.data.repository.database.RecentQueryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class RecentQueryUseCase @Inject constructor(private val recentQueryRepository: RecentQueryRepository) {

    val queries: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    suspend fun getRecentQuery() {
        recentQueryRepository.getRecentQuery().collect {
            queries.value = it
        }
    }

    suspend fun insert(query: String) {
        recentQueryRepository.addQuery(query)
    }

    suspend fun remove(query: String) {
        recentQueryRepository.removeQuery(query)
    }

}