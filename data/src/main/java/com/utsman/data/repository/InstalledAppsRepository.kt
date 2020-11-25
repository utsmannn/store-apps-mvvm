package com.utsman.data.repository

import com.utsman.data.model.dto.AppsSealedView

interface InstalledAppsRepository {
    fun getInstalledApps() : List<AppsSealedView.AppsView>
    suspend fun getInstalledAppsInStore(page: Int) : List<AppsSealedView.AppsView>?
}