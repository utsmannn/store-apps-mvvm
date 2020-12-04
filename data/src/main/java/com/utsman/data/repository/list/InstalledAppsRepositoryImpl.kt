/*
 * Created by Muhammad Utsman on 28/11/20 4:02 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.list

import android.content.Context
import android.content.pm.ApplicationInfo
import com.utsman.abstraction.ext.logi
import com.utsman.abstraction.ext.safeSingle
import com.utsman.data.R
import com.utsman.data.model.response.list.AppsItem
import com.utsman.data.model.dto.list.AppVersion
import com.utsman.data.model.dto.list.AppsSealedView
import com.utsman.data.model.dto.list.toAppsView
import com.utsman.data.route.Services
import java.lang.IndexOutOfBoundsException
import javax.inject.Inject

class InstalledAppsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val services: Services
) : InstalledAppsRepository {

    companion object {
        const val perPage = 10
    }

    private val installedAppsView get() = getInstalledApps()

    override fun getInstalledApps(): List<AppsSealedView.AppsView> {
        val packageManager = context.packageManager
        return packageManager.getInstalledApplications(0)
            .filter { app ->
                app.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) != 1
            }
            .map { aInfo ->
                packageManager.getPackageInfo(aInfo.packageName, 0)
            }
            .map { pInfo ->
                val vName = pInfo.versionName
                val vCode =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        pInfo.longVersionCode
                    } else {
                        pInfo.versionCode.toLong()
                    }

                val aVersion = AppVersion(name = vName, code = vCode)
                return@map AppsSealedView.AppsView.simple {
                    packageName = pInfo.packageName
                    appVersion = aVersion
                }
            }
    }

    override suspend fun getUpdatedAppsInStore(page: Int): List<AppsSealedView.AppsView>? {
        logi("load from -> $page until ${page+ perPage}, size is -> ${installedAppsView.size}")
        return if (page+ perPage <= installedAppsView.size) {
            try {
                installedAppsView.subList(page, page+ perPage).mapNotNull { app ->
                    services.searchList(app.packageName)
                        .datalist
                        ?.list
                        ?.filter { ap ->
                            ap != AppsItem()
                        }
                        ?.filter { ap ->
                            (ap.file?.vercode ?: 0L) > app.appVersion.code
                        }
                        ?.safeSingle()
                        ?.toAppsView().apply {
                            val appFound = this
                            val newAppVersion = appFound.apply {
                                this?.packageName = app.packageName
                                this?.appVersion?.name = app.appVersion.name
                                this?.appVersion?.code = app.appVersion.code
                            }

                            appFound?.appVersion = newAppVersion?.appVersion ?: AppVersion()
                        }
                }
            } catch (e: IndexOutOfBoundsException) {
                emptyList()
            }
        } else {
            null
        }
    }

    override suspend fun checkInstalledApps(appsView: AppsSealedView.AppsView): AppsSealedView.AppsView {
        val appInstalledFound = installedAppsView.find { ap ->
            ap.packageName == appsView.packageName
        }
        val version = appInstalledFound?.appVersion
        return if (version != null || version != AppVersion()) {
            appsView.apply {
                appVersion.name = version?.name ?: ""
                appVersion.code = version?.code ?: 0

                val updateRes = R.drawable.ic_fluent_cloud_download_24_regular
                val installedRes = R.drawable.ic_fluent_checkmark_underline_circle_20_regular
                if (appVersion.code != 0L) {
                    iconLabel = if (appVersion.apiCode > appVersion.code) {
                        updateRes
                    } else {
                        installedRes
                    }
                }
            }
            return appsView
        } else {
            appsView
        }
    }
}