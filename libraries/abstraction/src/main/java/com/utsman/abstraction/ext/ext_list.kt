package com.utsman.abstraction.ext

fun <T> List<T>.safeSingle(): T? {
    return when (size) {
        1 -> this[0]
        else -> null
    }
}