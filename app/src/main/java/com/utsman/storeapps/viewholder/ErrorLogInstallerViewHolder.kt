/*
 * Created by Muhammad Utsman on 20/12/20 4:49 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.data.model.dto.setting.ErrorLog
import com.utsman.storeapps.databinding.ItemErrorLogBinding

class ErrorLogInstallerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemErrorLogBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun binding(errorLog: ErrorLog) = binding.run {
        txtTitle.text = errorLog.name
        txtDate.text = errorLog.getDate()
        txtReason.text = errorLog.reason
        txtDir.text = "Location: ${errorLog.dir}"
    }
}