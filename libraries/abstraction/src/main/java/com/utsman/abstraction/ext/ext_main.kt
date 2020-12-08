/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.utsman.abstraction.ext

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.utsman.abstraction.BuildConfig
import java.io.File

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


fun Context.getUriFromFile(file: File): Uri =
    FileProvider.getUriForFile(this, "com.utsman.storeapps.fileprovider", file)

fun debug(action: () -> Unit) {
    if (BuildConfig.DEBUG) {
        action.invoke()
    }
}

