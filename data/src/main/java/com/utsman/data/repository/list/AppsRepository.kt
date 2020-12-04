/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.model.response.list.Aptoide

interface AppsRepository {
    suspend fun getTopApps(): Aptoide
    suspend fun getSearchApps(query: String, offset: Int, limit: Int = 25): Aptoide
}