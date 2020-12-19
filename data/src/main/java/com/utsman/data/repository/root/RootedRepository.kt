/*
 * Created by Muhammad Utsman on 18/12/20 8:53 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.root

import com.utsman.data.model.dto.rooted.CommandResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RootedRepository {
    fun rooted(): Boolean
    suspend fun installApk(dir: String): CommandResult
}