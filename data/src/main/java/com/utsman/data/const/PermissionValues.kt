/*
 * Created by Muhammad Utsman on 18/12/20 6:25 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.const

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import com.utsman.abstraction.extensions.getValueLazyOf
import com.utsman.data.di._context
import com.utsman.data.model.dto.permission.PermissionData
import com.utsman.network.di._moshi

object PermissionValues {

    private val context by getValueLazyOf(_context)
    private val moshi by getValueLazyOf(_moshi)

    private fun getPermissions(): List<PermissionData?> {
        val listType = Types.newParameterizedType(List::class.java, PermissionData::class.java)
        val adapter: JsonAdapter<List<PermissionData?>>? = moshi?.adapter(listType)
        val assetPath = "permission_values.json"
        val permissionJson = context?.assets?.open(assetPath)
            ?.bufferedReader()
            ?.use { it.readText() }

        return if (permissionJson != null) {
            adapter?.fromJson(permissionJson) ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun getPermission(permissionName: String): PermissionData? {
        val name = permissionName.split(".").last()
        return getPermissions().find { it?.string == name }
    }

    fun setPermissionData(permissions: List<String>): List<PermissionData?> {
        return permissions.mapNotNull { name ->
            getPermission(name)
        }
    }
}