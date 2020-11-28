/*
 * Created by Muhammad Utsman on 28/11/20 4:34 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.detail

import com.utsman.data.model.dto.list.AppVersion
import com.utsman.data.model.response.detail.AptoideMeta

fun AptoideMeta.toDetailView(): DetailView {
    return DetailView.simple {
        data?.let { d ->
            this.id = d.id ?: 0
            this.name = d.name ?: ""
            this.packageName = d.`package` ?: ""
            this.appVersion = AppVersion.fromMeta(d)
            this.downloads = d.stats?.downloads ?: 0
            this.icon = d.icon ?: ""
            this.image = d.graphic ?: ""
            this.images = d.media?.screenshots?.map { s -> s?.url ?: "" } ?: emptyList()
            this.description = d.media?.description ?: ""
            this.file = File.simple { file ->
                file.added = d.file?.added ?: ""
                file.sha1 = d.file?.signature?.sha1 ?: ""
                file.size = d.file?.filesize ?: 0L
                file.url = d.file?.path ?: ""
            }
            this.permissions = d.file?.usedPermissions?.map { it ?: "" } ?: emptyList()
            this.developer = Developer.simple { dev ->
                dev.name = d.developer?.name ?: ""
                dev.url = d.developer?.website ?: ""
            }
        }
    }
}