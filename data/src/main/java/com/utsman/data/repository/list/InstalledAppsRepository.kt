/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import com.utsman.data.model.dto.list.AppsSealedView

interface InstalledAppsRepository {
    fun getInstalledApps() : List<AppsSealedView.AppsView>
    suspend fun getUpdatedAppsInStore(page: Int) : List<AppsSealedView.AppsView>?
    suspend fun checkInstalledApps(appsView: AppsSealedView.AppsView): AppsSealedView.AppsView
}