/*
 * Created by Muhammad Utsman on 19/12/20 3:41 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.setting

data class SettingData(
    val type: TypeSetting,
    val value: Boolean
) {
    companion object {
        fun autoInstaller(value: Boolean = false) = SettingData(TypeSetting.INSTALLER, value)
        fun maturity(value: Boolean = false) = SettingData(TypeSetting.MATURITY, value)
    }
}