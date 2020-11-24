package com.utsman.network

import com.utsman.network.di.jsonBeautifier

fun Any.toJson(): String {
    return when (this) {
        is String -> jsonBeautifier.data.fromString(this)
        else -> jsonBeautifier.data.fromAny(this)
    }
}