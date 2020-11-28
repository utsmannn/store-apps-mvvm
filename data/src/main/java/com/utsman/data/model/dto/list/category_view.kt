/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.dto.list

enum class CategoryViewType {
    BANNER, REGULAR
}

sealed class CategorySealedView(val viewType: CategoryViewType) {
    data class CategoryBannerView(
        var name: String = "",
        var query: String? = null,
        var apps: List<AppsSealedView> = emptyList(),
        var image: String = "",
        var desc: String = ""
    ): CategorySealedView(CategoryViewType.BANNER) {
        companion object {
            fun simple(bannerView: CategoryBannerView.() -> Unit) = CategoryBannerView().apply(bannerView)
        }
    }

    data class CategoryView(
        var name: String = "",
        var query: String? = null,
        var apps: List<AppsSealedView> = emptyList()
    ): CategorySealedView(CategoryViewType.REGULAR) {
        companion object {
            fun simple(categoryView: CategoryView.() -> Unit) = CategoryView().apply(categoryView)
        }
    }
}