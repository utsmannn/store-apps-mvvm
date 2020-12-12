/*
 * Created by Muhammad Utsman on 12/12/20 11:46 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun <T>getValueOfSafety(instanceState: MutableStateFlow<T>): Lazy<T> {
    val immutable: StateFlow<T> = instanceState
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        immutable.value
    }
}

fun <T>getValueOf(instanceState: MutableStateFlow<T>): T {
    val immutable: StateFlow<T> = instanceState
    return immutable.value
}