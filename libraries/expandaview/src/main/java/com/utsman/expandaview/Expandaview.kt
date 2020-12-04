/*
 * Created by Muhammad Utsman on 4/12/20 6:35 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.expandaview

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isInvisible
import androidx.core.widget.NestedScrollView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class Expandaview(context: Context, attributeSet: AttributeSet) : NestedScrollView(context, attributeSet) {
    enum class Expandinism {
        EXPAND, COLLAPSE
    }

    var state = Expandinism.COLLAPSE

    fun expand(expand: Boolean) {
        if (expand) expand()
        else collapse()
    }

    init {
        state = if (height < 0) Expandinism.COLLAPSE
        else Expandinism.EXPAND

        val expandedStateAttr =
            context.obtainStyledAttributes(attributeSet, R.styleable.Expandaview)

        val expandedState = expandedStateAttr.getBoolean(R.styleable.Expandaview_expand, false)
        expandedStateAttr.recycle()

        if (!expandedState) {
            isInvisible = true
            post {
                collapse()
            }
        }
    }

    val isExpanded
        get() = state == Expandinism.EXPAND

    fun setExpanded(expand: Boolean) = run {
        if (expand) expand()
        else collapse()
    }

    fun toggleExpand() = run {
        if (state == Expandinism.COLLAPSE) expand()
        else collapse()
    }

    fun expand() = run {
        animate()
            .translationY(1f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(400)
            .withEndAction {
                state = Expandinism.EXPAND
                isInvisible = false
            }
            .start()
    }

    fun collapse() = run {
        val transY = height.unaryMinus()
        animate()
            .translationY(transY.toFloat())
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(400)
            .withEndAction {
                state = Expandinism.COLLAPSE
                isInvisible = false
            }
            .start()
    }
}