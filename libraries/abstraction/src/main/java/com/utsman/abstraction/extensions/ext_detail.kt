/*
 * Created by Muhammad Utsman on 28/11/20 6:40 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.extensions

import android.content.Context
import android.view.View

infix fun View.detailFor(packageName: String?) {
    setOnClickListener {
        context.intentTo("com.utsman.detail.ui.DetailActivity") {
            putExtra("package_name", packageName)
        }
    }
}

infix fun Context.detailFor(packageName: String?) {
    intentTo("com.utsman.detail.ui.DetailActivity") {
        putExtra("package_name", packageName)
    }
}