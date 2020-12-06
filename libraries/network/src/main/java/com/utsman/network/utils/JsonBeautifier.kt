/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network.utils

import com.squareup.moshi.JsonReader
import com.utsman.abstraction.dimanual.moduleOf
import com.utsman.network.di.moshi
import okio.Buffer

class JsonBeautifier {

    private val moshiModule by moduleOf(moshi)
    private val buffer = Buffer()
    private val adapter = moshiModule.adapter(Any::class.java).indent("    ")

    fun fromString(source: String): String {
        val reader = JsonReader.of(buffer.writeUtf8(source))
            .apply {
                isLenient = true
            }
        val data = reader.readJsonValue()
        return adapter.toJson(data)
    }

    fun fromAny(source: Any): String {
        return adapter.toJson(source)
    }

    fun <T: Any>toAny(string: String): T {
        val reader = JsonReader.of(buffer.writeUtf8(string))
            .apply {
                isLenient = true
            }
        return reader.readJsonValue() as T
    }

}