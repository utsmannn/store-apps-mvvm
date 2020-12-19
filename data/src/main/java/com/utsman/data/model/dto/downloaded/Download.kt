/*
 * Created by Muhammad Utsman on 17/12/20 6:13 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.downloaded

object Download {
    const val MAX_LEVEL = 10000

    enum class TypeStatus {
        PREPARING, DOWNLOADING, CANCELING, PAUSED, FAILED, SUCCESS, PENDING//, INSTALLING
    }

    data class Status(
        val type: TypeStatus,
        val message: String
    ) {
        companion object {
            fun preparing() = Status(TypeStatus.PREPARING, "Preparing...")
            fun downloading(reason: String = "Downloading") = Status(TypeStatus.DOWNLOADING, reason)
            fun canceling() = Status(TypeStatus.CANCELING, "Canceling...")
            fun paused() = Status(TypeStatus.PAUSED, "Paused")
            fun failed(reason: String = "Failed") = Status(TypeStatus.FAILED, reason)
            fun success() = Status(TypeStatus.SUCCESS, "Success")
            fun pending(reason: String = "Pending") = Status(TypeStatus.PENDING, reason)
            //fun installing(reason: String = "Installing...") = Status(TypeStatus.INSTALLING, reason)
        }
    }
}