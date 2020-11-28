/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.model.response.list.Datalist

interface PagingAppRepository {
    suspend fun loadApps(query: String? = null, search: Boolean, offset: Int): Datalist
}