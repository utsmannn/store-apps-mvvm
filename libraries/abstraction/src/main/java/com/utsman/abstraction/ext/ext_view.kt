package com.utsman.abstraction.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.utsman.abstraction.dto.ResultState
import com.utsman.abstraction.listener.IResultState

infix fun ViewGroup.inflate(layoutResId: Int): View = LayoutInflater.from(context).inflate(layoutResId, this, false)

fun Long.bytesToString() = when {
    this == Long.MIN_VALUE || this < 0 -> "N/A"
    this < 1024L -> "$this B"
    this <= 0xfffccccccccccccL shr 40 -> "%.1f KB".format(this.toDouble() / (0x1 shl 10))
    this <= 0xfffccccccccccccL shr 30 -> "%.1f MB".format(this.toDouble() / (0x1 shl 20))
    this <= 0xfffccccccccccccL shr 20 -> "%.1f GB".format(this.toDouble() / (0x1 shl 30))
    this <= 0xfffccccccccccccL shr 10 -> "%.1f TB".format(this.toDouble() / (0x1 shl 40))
    this <= 0xfffccccccccccccL -> "%.1f PiB".format((this shr 10).toDouble() / (0x1 shl 40))
    else -> "%.1f EiB".format((this shr 20).toDouble() / (0x1 shl 40))
}

fun <T: Any>ResultState<T>.bindToProgressView(view: View) {
    view.isVisible = this is ResultState.Loading
}