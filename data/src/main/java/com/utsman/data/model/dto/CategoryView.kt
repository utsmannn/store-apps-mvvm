package com.utsman.data.model.dto

data class CategoryView(
    var name: String = "",
    var apps: List<AppsView> = emptyList()
) {
    companion object {
        fun simple(categoryView: CategoryView.() -> Unit) = CategoryView().apply(categoryView)
    }
}