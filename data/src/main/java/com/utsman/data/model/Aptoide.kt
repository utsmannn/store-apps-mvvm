package com.utsman.data.model

import com.squareup.moshi.Json

data class Aptoide(

    @Json(name="datalist")
    val datalist: Datalist? = null,

    @Json(name="info")
    val info: Info? = null
)

data class File(

    @Json(name="md5sum")
    val md5sum: String? = null,

    @Json(name="filesize")
    val filesize: Int? = null,

    @Json(name="vername")
    val vername: String? = null,

    @Json(name="vercode")
    val vercode: Int? = null,

    @Json(name="tags")
    val tags: List<Any?>? = null
)

data class Info(

    @Json(name="time")
    val time: Time? = null,

    @Json(name="status")
    val status: String? = null
)

data class Rating(

    @Json(name="total")
    val total: Int? = null,

    @Json(name="avg")
    val avg: Double? = null
)

data class AppsItem(

    @Json(name="package")
    val jsonMemberPackage: String? = null,

    @Json(name="uname")
    val uname: String? = null,

    @Json(name="added")
    val added: String? = null,

    @Json(name="icon")
    val icon: String? = null,

    @Json(name="store")
    val store: Store? = null,

    @Json(name="obb")
    val obb: Any? = null,

    @Json(name="appcoins")
    val appcoins: Appcoins? = null,

    @Json(name="uptype")
    val uptype: String? = null,

    @Json(name="file")
    val file: File? = null,

    @Json(name="size")
    val size: Long? = null,

    @Json(name="stats")
    val stats: Stats? = null,

    @Json(name="name")
    val name: String? = null,

    @Json(name="modified")
    val modified: String? = null,

    @Json(name="id")
    val id: Int? = null,

    @Json(name="graphic")
    val graphic: String? = null,

    @Json(name="updated")
    val updated: String? = null
)

data class Stats(

    @Json(name="prating")
    val prating: Prating? = null,

    @Json(name="pdownloads")
    val pdownloads: Long? = null,

    @Json(name="downloads")
    val downloads: Long? = null,

    @Json(name="rating")
    val rating: Rating? = null
)

data class Obb(

    @Json(name="patch")
    val patch: Patch? = null,

    @Json(name="main")
    val main: Main? = null
)

data class Main(

    @Json(name="filename")
    val filename: String? = null,

    @Json(name="md5sum")
    val md5sum: String? = null,

    @Json(name="filesize")
    val filesize: Int? = null
)

data class Prating(

    @Json(name="total")
    val total: Int? = null,

    @Json(name="avg")
    val avg: Double? = null
)

data class Store(

    @Json(name="name")
    val name: String? = null,

    @Json(name="id")
    val id: Int? = null,

    @Json(name="avatar")
    val avatar: String? = null
)

data class Time(

    @Json(name="seconds")
    val seconds: Double? = null,

    @Json(name="human")
    val human: String? = null
)

data class Datalist(

    @Json(name="next")
    val next: Int? = null,

    @Json(name="loaded")
    val loaded: Boolean? = null,

    @Json(name="total")
    val total: Int? = null,

    @Json(name="offset")
    val offset: Int? = null,

    @Json(name="hidden")
    val hidden: Int? = null,

    @Json(name="count")
    val count: Int? = null,

    @Json(name="limit")
    val limit: Int? = null,

    @Json(name="list")
    val list: List<AppsItem>? = emptyList()
)

data class Patch(

    @Json(name="filename")
    val filename: String? = null,

    @Json(name="md5sum")
    val md5sum: String? = null,

    @Json(name="filesize")
    val filesize: Int? = null
)

data class Appcoins(

    @Json(name="advertising")
    val advertising: Boolean? = null,

    @Json(name="billing")
    val billing: Boolean? = null
)
