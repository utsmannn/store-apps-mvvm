package com.utsman.data.model.dto

enum class AppsViewType {
    BANNER, REGULAR
}

sealed class AppsSealedView(val viewType: AppsViewType) {
    data class AppsBannerView(
        var id: Int = 0,
        var name: String = "",
        var downloads: Long = 0,
        var size: Long = 0,
        var icon: String = "",
        var image: String = ""
    ) : AppsSealedView(AppsViewType.BANNER) {
        companion object {
            fun simple(bannerView: AppsBannerView.() -> Unit) = AppsBannerView().apply(bannerView)
        }
    }

    data class AppsView(
        var id: Int = 0,
        var name: String = "",
        var size: Long = 0,
        var icon: String = ""
    ): AppsSealedView(AppsViewType.REGULAR) {
        companion object {
            fun simple(appsView: AppsView.() -> Unit) = AppsView().apply(appsView)
        }
    }
}