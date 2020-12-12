/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model

import com.utsman.abstraction.extensions.capital

data class Category(
    var name: String = "",
    var query: String = "",
    var image: String = "",
    var desc: String = ""
) {
    companion object {
        fun simple(category: Category.() -> Unit) = Category().apply(category)
        fun buildFrom(query: String) : Category {
            val name = query.replace("-", " & ")
                .capital()
            return Category(name, query)
        }
    }
}