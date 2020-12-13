/*
 * Created by Muhammad Utsman on 12/12/20 11:46 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.NullPointerException

fun <T>getValueLazyOf(instanceState: MutableStateFlow<T>): Lazy<T> {
    val immutable: StateFlow<T> = instanceState
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        immutable.value
    }
}

inline fun <reified T: Any?>getValueOf(instanceState: MutableStateFlow<T?>): T {
    val immutable: StateFlow<T?> = instanceState
    val className = T::class.simpleName
    return try {
        immutable.value!!
    } catch (e: ExceptionInInitializerError) {
        loge("Module not yet initialize. Check module of class : `$className`")
        throw e
    }
}

inline fun <reified T>getValueSafeOf(instanceState: MutableStateFlow<T>): T {
    val immutable: StateFlow<T> = instanceState
    return immutable.value
}