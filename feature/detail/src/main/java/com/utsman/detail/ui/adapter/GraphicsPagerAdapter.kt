/*
 * Created by Muhammad Utsman on 18/12/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.utsman.abstraction.extensions.loadUrl
import com.utsman.detail.R
import com.utsman.detail.databinding.ItemGraphicFullBinding

class GraphicsPagerAdapter(private val graphics: List<String?>) : PagerAdapter() {
    override fun getCount(): Int {
        return graphics.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_graphic_full, null)
        ItemGraphicFullBinding.bind(view).run {
            graphics[position]?.let { item ->
                imgGraphicFull.loadUrl(item, item, colorInt = R.color.purple_200)
            }
        }

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}