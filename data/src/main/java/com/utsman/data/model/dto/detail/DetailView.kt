package com.utsman.data.model.dto.detail

import com.utsman.data.model.dto.list.AppVersion

data class DetailView(
    var id: Int = 0,
    var name: String = "",
    var packageName: String = "",
    var appVersion: AppVersion = AppVersion(),
    var downloads: Long = 0,
    var size: Long = 0,
    var icon: String = "",
    var image: String = ""
)