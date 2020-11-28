/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.list

import com.utsman.data.model.response.list.AppsItem
import com.utsman.data.model.response.list.Aptoide
import com.utsman.data.model.Category

fun Aptoide.toCategoryBannerView(category: Category?): CategorySealedView.CategoryBannerView? {
    val list = datalist?.list?.map { app ->
        AppsSealedView.AppsView.simple {
            id = app.id ?: 0
            name = app.name ?: ""
            packageName = app.`package` ?: ""
            size = app.size ?: 0
            icon = app.icon ?: ""
            appVersion = AppVersion.fromItem(app)
        }
    } ?: emptyList()

    return CategorySealedView.CategoryBannerView.simple {
        name = category?.name ?: ""
        query = category?.query
        apps = list
        image = category?.image ?: ""
        desc = category?.desc ?: ""
    }
}

fun Aptoide.toCategoryView(category: Category?): CategorySealedView.CategoryView? {
    val list = datalist?.list?.map { app ->
        AppsSealedView.AppsView.simple {
            id = app.id ?: 0
            name = app.name ?: ""
            packageName = app.`package` ?: ""
            size = app.size ?: 0
            icon = app.icon ?: ""
            appVersion = AppVersion.fromItem(app)
        }
    } ?: emptyList()

    return CategorySealedView.CategoryView.simple {
        name = category?.name ?: ""
        query = category?.query
        apps = list
    }
}

fun AppsItem.toAppsBannerView(): AppsSealedView.AppsBannerView {
    return AppsSealedView.AppsBannerView.simple {
        id = this@toAppsBannerView.id ?: 0
        name = this@toAppsBannerView.name ?: ""
        packageName = this@toAppsBannerView.`package` ?: ""
        downloads = this@toAppsBannerView.stats?.downloads ?: 0
        size = this@toAppsBannerView.size ?: 0
        icon = this@toAppsBannerView.icon ?: ""
        image = this@toAppsBannerView.graphic ?: ""
        appVersion = AppVersion.fromItem(this@toAppsBannerView)
    }
}

fun AppsItem.toAppsView(): AppsSealedView.AppsView {
    return AppsSealedView.AppsView.simple {
        id = this@toAppsView.id ?: 0
        name = this@toAppsView.name ?: ""
        packageName = this@toAppsView.`package` ?: ""
        size = this@toAppsView.size ?: 0
        icon = this@toAppsView.icon ?: ""
        appVersion = AppVersion.fromItem(this@toAppsView)
    }
}