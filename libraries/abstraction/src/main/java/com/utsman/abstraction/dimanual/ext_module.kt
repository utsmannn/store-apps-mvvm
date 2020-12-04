/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.dimanual

fun <T: Any> moduleOf(mod: Module<T>): Lazy<T> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        mod.data
    }
}