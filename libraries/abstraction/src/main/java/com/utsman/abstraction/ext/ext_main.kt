@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.utsman.abstraction.ext

import android.util.Log
import com.utsman.abstraction.BuildConfig

fun loge(msg: String?) = Log.e("Store_Apps ---------", msg)
fun logi(msg: String?, tag: String = "Store_Apps ---------") {
    debug {
        val maxLogSize = 1000
        val nullableMsg = msg ?: ""
        for (i in 0..nullableMsg.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > nullableMsg.length) nullableMsg.length else end
            Log.i(tag, nullableMsg.substring(start, end))
        }
    }
}

fun debug(action: () -> Unit) {
    if (BuildConfig.DEBUG) {
        action.invoke()
    }
}

