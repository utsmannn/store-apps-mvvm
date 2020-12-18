/*
 * Created by Muhammad Utsman on 18/12/20 11:52 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getDrawableRes(drawableRes: Int, color: Int? = null): Drawable? {
    val originDrawable = ContextCompat.getDrawable(this, drawableRes)
    return if (color != null) {
        if (originDrawable != null) {
            val copyDrawable = DrawableCompat.wrap(originDrawable)
            DrawableCompat.setTint(copyDrawable, color)
            copyDrawable
        } else {
            originDrawable
        }
    } else {
        originDrawable
    }
}