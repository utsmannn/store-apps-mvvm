package com.utsman.abstraction.di

fun <T: Any> moduleOf(mod: Module<T>): Lazy<T> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        mod.data
    }
}