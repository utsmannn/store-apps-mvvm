/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.list

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.data.model.dto.worker.WorkInfoResult

enum class AppsViewType {
    BANNER, REGULAR
}

sealed class AppsSealedView(val viewType: AppsViewType) {
    data class AppsBannerView(
        var id: Int = 0,
        var name: String = "",
        var packageName: String = "",
        var appVersion: AppVersion = AppVersion(),
        var downloads: Long = 0,
        var size: Long = 0,
        var icon: String = "",
        var image: String = "",
        var iconLabel: Int? = null
    ) : AppsSealedView(AppsViewType.BANNER) {
        companion object {
            fun simple(bannerView: AppsBannerView.() -> Unit) = AppsBannerView().apply(bannerView)
        }
    }

    data class AppsView(
        var id: Int = 0,
        var name: String = "",
        var packageName: String = "",
        var appVersion: AppVersion = AppVersion(),
        var size: Long = 0,
        var icon: String = "",
        var iconLabel: Int? = null,
        var downloadedApps: DownloadedApps? = null
    ) : AppsSealedView(AppsViewType.REGULAR) {
        companion object {
            fun simple(appsView: AppsView.() -> Unit) = AppsView().apply(appsView)
        }
    }
}