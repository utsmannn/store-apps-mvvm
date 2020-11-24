package com.utsman.data.model.dto

data class AppsView(
    var id: Int = 0,
    var name: String = "",
    var size: Long = 0,
    var icon: String = ""
) {
    companion object {
        fun simple(appsView: AppsView.() -> Unit) = AppsView().apply(appsView)
    }
}