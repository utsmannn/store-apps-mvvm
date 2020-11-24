package com.utsman.data.model.dto

import com.utsman.data.model.AppsItem
import com.utsman.data.model.Aptoide
import com.utsman.data.model.Category

fun Aptoide.toCategoryView(category: Category): CategoryView {
    val list = datalist?.list?.map { app ->
        AppsView.simple {
            id = app.id ?: 0
            name = app.name ?: ""
            size = app.size ?: 0
            icon = app.icon ?: ""
        }
    } ?: emptyList()

    return CategoryView.simple {
        name = category.name
        apps = list
    }
}

fun AppsItem.toAppsView(): AppsView {
    return AppsView.simple {
        id = this@toAppsView.id ?: 0
        name = this@toAppsView.name ?: ""
        size = this@toAppsView.size ?: 0
        icon = this@toAppsView.icon ?: ""
    }
}