/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network

import com.utsman.network.di.jsonBeautifier

fun Any.toJson(): String {
    return when (this) {
        is String -> jsonBeautifier.data.fromString(this)
        else -> jsonBeautifier.data.fromAny(this)
    }
}