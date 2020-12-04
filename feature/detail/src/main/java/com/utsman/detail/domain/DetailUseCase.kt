/*
 * Created by Muhammad Utsman on 28/11/20 5:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.domain

import com.utsman.abstraction.dto.ResultState
import com.utsman.abstraction.dto.fetch
import com.utsman.abstraction.dto.stateOf
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.model.dto.detail.toDetailView
import com.utsman.data.repository.meta.MetaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailUseCase @Inject constructor(private val metaRepository: MetaRepository) {

    val detailView = stateOf<DetailView>()

    suspend fun getDetail(scope: CoroutineScope, packageName: String) = scope.launch {
        fetch {
            val response = metaRepository.getDetail(packageName)
            response?.toDetailView() ?: DetailView()
        }.collect {
            detailView.value = it
        }
    }

    fun restartState(scope: CoroutineScope) = scope.launch {
        detailView.value = ResultState.Idle()
    }
}