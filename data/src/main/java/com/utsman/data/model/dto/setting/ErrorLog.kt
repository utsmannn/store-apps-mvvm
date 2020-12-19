/*
 * Created by Muhammad Utsman on 20/12/20 4:38 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.setting

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

data class ErrorLog(
    val name: String?,
    val dir: String,
    val reason: String,
    val millis: Long
) {
    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val dateFormat = "dd/MM/yyyy hh:mm:ss"
        val formatter = SimpleDateFormat(dateFormat)
        return formatter.format(Date(millis))
    }
}