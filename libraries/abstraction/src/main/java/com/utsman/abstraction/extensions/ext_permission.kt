/*
 * Created by Muhammad Utsman on 8/12/20 3:23 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.extensions

import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Context.withPermissions(listPermission: List<String>, action: (grantedList: List<String>, deniedList: List<String>) -> Unit) {
    Dexter.withContext(this)
        .withPermissions(listPermission)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    action.invoke(
                        report.grantedPermissionResponses.map { it.permissionName },
                        report.deniedPermissionResponses.map { it.permissionName })
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }

        })
        .check()
}