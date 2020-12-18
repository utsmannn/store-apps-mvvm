/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.detail

import com.utsman.data.model.dto.list.AppVersion
import com.utsman.data.model.dto.permission.PermissionData

data class DetailView(
    var id: Int = 0,
    var name: String = "",
    var packageName: String = "",
    var appVersion: AppVersion = AppVersion(),
    var downloads: Long = 0,
    var icon: String = "",
    var image: String = "",
    var images: List<String> = emptyList(),
    var description: String = "",
    var file: File = File(),
    var permissions: List<PermissionData?> = emptyList(),
    var developer: Developer = Developer()
) {
    companion object {
        fun simple(detailView: DetailView.() -> Unit) = DetailView().apply(detailView)
    }
}

data class Developer(
    var name: String = "",
    var url: String = ""
) {
    companion object {
        fun simple(developer: (Developer) -> Unit) = Developer().apply(developer)
    }
}

data class File(
    var url: String = "",
    var sha1: String = "",
    var size: Long = 0L,
    var added: String = ""
) {
    companion object {
        fun simple(file: (File) -> Unit) = File().apply(file)
    }
}