package com.utsman.data.model.dto

import com.utsman.data.model.AppsItem

data class AppVersion(
    var name: String = "",
    var code: Long = 0,
    var apiName: String = "",
    var apiCode: Long = 0
) {
    companion object {
        fun from(appsItem: AppsItem?) = AppVersion(
            apiName = appsItem?.file?.vername ?: "",
            apiCode = appsItem?.file?.vercode ?: 0
        )
    }
}