/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network

import com.utsman.abstraction.extensions.getValueLazyOf
import com.utsman.network.di._jsonBeautifier

private val jsonBeauty by getValueLazyOf(_jsonBeautifier)

fun Any.toJson(): String {
    return when (this) {
        is String -> jsonBeauty?.fromString(this) ?: ""
        else -> jsonBeauty?.fromAny(this, Any::class.java) ?: ""
    }
}

fun <T: Any>String.toAny(type: Class<T>): T? {
    return jsonBeauty?.toAny(this, type)
}

fun <T: Any>String.toAnyList(type: Class<T>): List<T>? {
    return jsonBeauty?.toAnyList(this, type)
}

fun <T: Any>List<T>.toJsonList(type: Class<T>): String? {
    return jsonBeauty?.fromAnyList(this, type)
}