/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.ext

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.ObjectKey
import com.utsman.abstraction.base.GlideApp

private const val CROSS_FADE_DURATION = 350

fun ImageView.loadUrl(
    url: String,
    id: String,
    colorInt: Int? = null,
    colorString: String? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    val sign = ObjectKey(id)
    colorInt?.let { background = ColorDrawable(it) }
    colorString?.let { background = ColorDrawable(Color.parseColor(it)) }
    GlideApp.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .addListener(requestListener)
        .signature(sign)
        .into(this)
        .clearOnDetach()
}

fun ImageView.loadRes(
    @DrawableRes res: Int?,
    id: String,
    requestListener: RequestListener<Drawable>? = null,
    placeholder: Drawable? = null
) = run {
    val sign = ObjectKey(id)
    GlideApp.with(context)
        .load(res)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .addListener(requestListener)
        .placeholder(placeholder)
        .signature(sign)
        .into(this)
        .clearOnDetach()
}