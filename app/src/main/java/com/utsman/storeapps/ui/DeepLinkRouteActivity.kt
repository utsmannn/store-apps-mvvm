/*
 * Created by Muhammad Utsman on 22/12/20 9:51 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utsman.abstraction.extensions.detailFor

class DeepLinkRouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent?.data

        val dataString = data.toString()
        val packageApp = when {
            (dataString.contains("id=")) -> dataString.substringAfter("id=").substringBefore("&")
            (dataString.contains("id%3D")) -> dataString.substringAfter("id%3D")
                .substringBefore("#")
            else -> null
        }

        if (packageApp != null) {
            this detailFor packageApp
        }
        finish()
    }
}