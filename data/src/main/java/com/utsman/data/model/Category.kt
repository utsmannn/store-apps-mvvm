package com.utsman.data.model

data class Category(
    var name: String = "",
    var query: String = ""
) {
    companion object {
        fun simple(category: Category.() -> Unit) = Category().apply(category)
    }
}