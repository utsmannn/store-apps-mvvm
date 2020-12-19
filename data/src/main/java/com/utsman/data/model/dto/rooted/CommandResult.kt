/*
 * Created by Muhammad Utsman on 18/12/20 8:58 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.rooted

data class CommandResult(
    val success: Boolean = false,
    val message: String? = ""
)