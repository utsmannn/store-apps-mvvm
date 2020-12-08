/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network

import com.utsman.network.di.jsonBeautifier

fun Any.toJson(): String {
    return when (this) {
        is String -> jsonBeautifier.data.fromString(this)
        else -> jsonBeautifier.data.fromAny(this, Any::class.java)
    }
}

fun <T: Any>String.toAny(type: Class<T>): T? {
    return jsonBeautifier.data.toAny(this, type)
}

fun <T: Any>String.toAnyList(type: Class<T>): List<T>? {
    return jsonBeautifier.data.toAnyList(this, type)
}