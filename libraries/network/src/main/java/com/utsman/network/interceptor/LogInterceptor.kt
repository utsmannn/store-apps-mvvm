package com.utsman.network.interceptor

import com.squareup.moshi.JsonEncodingException
import com.utsman.abstraction.ext.logi
import com.utsman.network.toJson
import okhttp3.logging.HttpLoggingInterceptor

class LogInterceptor : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val tag = "API LOGGING ------"
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                //logi(message.toJson(), tag = tag)
            } catch (e: JsonEncodingException) {
                logi("..failed to logging response..")
            }
        } else {
            logi(message, tag = tag)
            return
        }
    }
}