/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.const

import com.utsman.data.model.Category
import java.lang.IndexOutOfBoundsException

object CategoriesApps {

    val list = listOf(
        "tools", "music", "social", "shopping", "productivity", "business", "education",
        "entertainment", "family", "lifestyle", "travel", "weather", "auto-vehicles",
        "photography"
    ).map { Category.buildFrom(it) }

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