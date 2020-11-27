package com.utsman.data.repository

import com.utsman.data.model.dto.list.AppsSealedView

interface InstalledAppsRepository {
    fun getInstalledApps() : List<AppsSealedView.AppsView>
    suspend fun getUpdatedAppsInStore(page: Int) : List<AppsSealedView.AppsView>?
    suspend fun checkInstalledApps(appsView: AppsSealedView.AppsView): AppsSealedView.AppsView
}