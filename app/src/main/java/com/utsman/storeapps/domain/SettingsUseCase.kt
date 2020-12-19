/*
 * Created by Muhammad Utsman on 18/12/20 9:26 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.domain

import com.utsman.data.model.dto.rooted.CommandResult
import com.utsman.data.repository.root.RootedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val rootedRepository: RootedRepository
) {
    val isRooted = rootedRepository.rooted()
}