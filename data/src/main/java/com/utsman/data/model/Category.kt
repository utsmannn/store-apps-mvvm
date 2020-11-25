package com.utsman.data.model

import com.utsman.abstraction.ext.capital
import java.util.*

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