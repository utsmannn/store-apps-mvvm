package com.utsman.data.repository

import com.utsman.data.model.Aptoide

interface AppsRepository {
    suspend fun getRandomApps(): Aptoide
}