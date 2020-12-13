/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network.utils

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.utsman.abstraction.extensions.getValueLazyOf
import com.utsman.network.di._moshi
import okio.Buffer
import javax.inject.Inject

class JsonBeautifier @Inject constructor(private val moshi: Moshi) {

    private val buffer = Buffer()
    private val adapter = moshi.adapter(Any::class.java)?.indent("    ")

    fun fromString(source: String): String {
        val reader = JsonReader.of(buffer.writeUtf8(source))
            .apply {
                isLenient = true
            }
        return try {
            val data = reader.readJsonValue()
            adapter?.toJson(data) ?: "[ ]"
        } catch (e: JsonEncodingException) {
            "[ ]"
        }
    }

    fun <T: Any>fromAny(source: Any, type: Class<T>): String {
        val anyAdapter = moshi.adapter(type)
        val beautyResult = anyAdapter?.toJson(source as T)
        return fromString(beautyResult ?: "")
    }

    fun <T: Any>toAny(string: String, type: Class<T>): T? {
        val moshiAdapter = moshi.adapter(type)
        return moshiAdapter?.fromJson(string)
    }

    fun <T: Any>toAnyList(string: String, type: Class<T>): List<T>? {
        val typeParam = Types.newParameterizedType(List::class.java, type)
        val adapter = moshi.adapter<List<T>>(typeParam)
        return adapter?.fromJson(string)
    }

    fun <T: Any>fromAnyList(source: List<T>, type: Class<T>): String {
        val typeParam = Types.newParameterizedType(List::class.java, type)
        val adapter = moshi.adapter<List<T>>(typeParam)
        return adapter?.toJson(source) ?: ""
    }

}