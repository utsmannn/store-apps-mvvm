/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.list

import com.utsman.data.model.response.detail.Data
import com.utsman.data.model.response.list.AppsItem

data class AppVersion(
    var name: String = "",
    var code: Long = 0,
    var apiName: String = "",
    var apiCode: Long = 0,
    var pathUrl: String? = null
) {
    companion object {
        fun fromItem(appsItem: AppsItem?) = AppVersion(
            apiName = appsItem?.file?.vername ?: "",
            apiCode = appsItem?.file?.vercode ?: 0,
            pathUrl = appsItem?.file?.path
        )
        fun fromMeta(data: Data?) = AppVersion(
            apiName = data?.file?.vername ?: "",
            apiCode = data?.file?.vercode ?: 0,
            pathUrl = data?.file?.path
        )
    }
}