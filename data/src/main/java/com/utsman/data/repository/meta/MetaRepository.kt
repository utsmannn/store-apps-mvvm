/*
 * Created by Muhammad Utsman on 28/11/20 4:03 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.meta

import com.utsman.data.model.response.detail.AptoideMeta

interface MetaRepository {
    suspend fun getDetail(packageName: String): AptoideMeta?
}