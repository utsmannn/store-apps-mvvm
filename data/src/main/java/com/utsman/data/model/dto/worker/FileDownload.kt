/*
 * Created by Muhammad Utsman on 8/12/20 3:28 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.worker

data class FileDownload(
    var name: String? = "",
    var url: String? = "",
    var packageName: String? = "",
    var fileName: String? = ""
) {
    companion object {
        fun simple(download: FileDownload.() -> Unit) = FileDownload().apply(download)
    }
}