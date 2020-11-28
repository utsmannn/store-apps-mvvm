/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.ext

fun <T> List<T>.safeSingle(): T? {
    return when (size) {
        1 -> this[0]
        else -> null
    }
}