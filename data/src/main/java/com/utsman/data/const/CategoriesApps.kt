/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.const

import com.utsman.data.R
import com.utsman.data.model.Category
import java.lang.IndexOutOfBoundsException

object CategoriesApps {

    private val listIconRes = listOf(
        R.drawable.ic_fluent_toolbox_24_filled,
        R.drawable.ic_fluent_music_note_24_filled,
        R.drawable.ic_fluent_person_feedback_24_filled,
        R.drawable.ic_fluent_cart_24_filled,
        R.drawable.ic_fluent_calligraphy_pen_24_filled,
        R.drawable.ic_fluent_briefcase_24_filled,
        R.drawable.ic_fluent_book_24_filled,
        R.drawable.ic_fluent_bot_24_filled,
        R.drawable.ic_fluent_food_cake_24_filled,
        R.drawable.ic_fluent_glasses_24_filled,
        R.drawable.ic_fluent_airplane_24_filled,
        R.drawable.ic_fluent_cloud_24_filled,
        R.drawable.ic_fluent_vehicle_car_24_filled,
        R.drawable.ic_fluent_camera_24_filled
    )

    private val listRaw = listOf(
        "tools",
        "music",
        "social",
        "shopping",
        "productivity",
        "business",
        "education",
        "entertainment",
        "family",
        "lifestyle",
        "travel",
        "weather",
        "auto-vehicles",
        "photography"
    )

    val list = listRaw.mapIndexed { index, s ->
        Category.buildFrom(s, listIconRes[index])
    }

    // mock paging for category
    object MockPaging {

        fun page(page: Int): List<Category>? {
            val perPage = 2
            return try {
                list.subList(page, page+perPage)
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }
    }
}