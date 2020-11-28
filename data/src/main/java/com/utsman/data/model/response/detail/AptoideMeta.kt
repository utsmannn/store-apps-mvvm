/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.model.response.detail

import com.squareup.moshi.Json

data class AptoideMeta(

	@Json(name="data")
	val data: Data? = null,

	@Json(name="info")
	val info: Info? = null
)

data class Reason(

	@Json(name="reason")
	val reason: Any? = null,

	@Json(name="added")
	val added: Any? = null,

	@Json(name="scanned")
	val scanned: Scanned? = null,

	@Json(name="modified")
	val modified: Any? = null,

	@Json(name="manual_qa")
	val manualQa: ManualQa? = null,

	@Json(name="signature_validated")
	val signatureValidated: SignatureValidated? = null
)

data class Malware(

	@Json(name="reason")
	val reason: Reason? = null,

	@Json(name="added")
	val added: String? = null,

	@Json(name="rank")
	val rank: String? = null,

	@Json(name="modified")
	val modified: String? = null
)

data class Flags(

	@Json(name="review")
	val review: String? = null
)

data class Scanned(

	@Json(name="date")
	val date: String? = null,

	@Json(name="av_info")
	val avInfo: List<AvInfoItem?>? = null,

	@Json(name="status")
	val status: String? = null
)

data class File(

	@Json(name="malware")
	val malware: Malware? = null,

	@Json(name="added")
	val added: String? = null,

	@Json(name="signature")
	val signature: Signature? = null,

	@Json(name="flags")
	val flags: Flags? = null,

	@Json(name="filesize")
	val filesize: Long? = null,

	@Json(name="used_permissions")
	val usedPermissions: List<String?>? = null,

	@Json(name="tags")
	val tags: List<String?>? = null,

	@Json(name="path")
	val path: String? = null,

	@Json(name="used_features")
	val usedFeatures: List<String?>? = null,

	@Json(name="md5sum")
	val md5sum: String? = null,

	@Json(name="path_alt")
	val pathAlt: String? = null,

	@Json(name="vername")
	val vername: String? = null,

	@Json(name="vercode")
	val vercode: Long? = null,

	@Json(name="hardware")
	val hardware: Hardware? = null
)

data class DependenciesItem(

	@Json(name="level")
	val level: String? = null,

	@Json(name="type")
	val type: String? = null
)

data class Signature(

	@Json(name="sha1")
	val sha1: String? = null,

	@Json(name="owner")
	val owner: String? = null
)

data class VotesItem(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="value")
	val value: Int? = null
)

data class Prating(

	@Json(name="total")
	val total: Int? = null,

	@Json(name="avg")
	val avg: Double? = null,

	@Json(name="votes")
	val votes: List<VotesItem?>? = null
)

data class SignatureValidated(

	@Json(name="date")
	val date: String? = null,

	@Json(name="signature_from")
	val signatureFrom: String? = null,

	@Json(name="status")
	val status: String? = null
)

data class Store(

	@Json(name="appearance")
	val appearance: Appearance? = null,

	@Json(name="stats")
	val stats: Stats? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="avatar")
	val avatar: String? = null
)

data class Media(

	@Json(name="summary")
	val summary: String? = null,

	@Json(name="news")
	val news: String? = null,

	@Json(name="keywords")
	val keywords: List<String?>? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="videos")
	val videos: List<Any?>? = null,

	@Json(name="screenshots")
	val screenshots: List<ScreenshotsItem?>? = null
)

data class Data(

	@Json(name="package")
	val `package`: String? = null,

	@Json(name="aab")
	val aab: Any? = null,

	@Json(name="uname")
	val uname: String? = null,

	@Json(name="added")
	val added: String? = null,

	@Json(name="main_package")
	val mainPackage: Any? = null,

	@Json(name="icon")
	val icon: String? = null,

	@Json(name="pay")
	val pay: Any? = null,

	@Json(name="store")
	val store: Store? = null,

	@Json(name="media")
	val media: Media? = null,

	@Json(name="obb")
	val obb: Any? = null,

	@Json(name="appcoins")
	val appcoins: Appcoins? = null,

	@Json(name="urls")
	val urls: Urls? = null,

	@Json(name="file")
	val file: File? = null,

	@Json(name="size")
	val size: Int? = null,

	@Json(name="stats")
	val stats: Stats? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="modified")
	val modified: String? = null,

	@Json(name="developer")
	val developer: Developer? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="graphic")
	val graphic: String? = null,

	@Json(name="updated")
	val updated: String? = null,

	@Json(name="age")
	val age: Age? = null
)

data class Age(

	@Json(name="pegi")
	val pegi: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="rating")
	val rating: Int? = null,

	@Json(name="title")
	val title: String? = null
)

data class Developer(

	@Json(name="website")
	val website: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="privacy")
	val privacy: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="email")
	val email: String? = null
)

data class AvInfoItem(

	@Json(name="infections")
	val infections: List<Any?>? = null,

	@Json(name="name")
	val name: String? = null
)

data class Rating(

	@Json(name="total")
	val total: Int? = null,

	@Json(name="avg")
	val avg: Double? = null,

	@Json(name="votes")
	val votes: List<VotesItem?>? = null
)

data class Appearance(

	@Json(name="description")
	val description: String? = null,

	@Json(name="theme")
	val theme: String? = null
)

data class ScreenshotsItem(

	@Json(name="width")
	val width: Int? = null,

	@Json(name="url")
	val url: String? = null,

	@Json(name="height")
	val height: Int? = null
)

data class Urls(

	@Json(name="w")
	val W: String? = null,

	@Json(name="m")
	val M: String? = null
)

data class Hardware(

	@Json(name="gles")
	val gles: Int? = null,

	@Json(name="cpus")
	val cpus: List<String?>? = null,

	@Json(name="screen")
	val screen: String? = null,

	@Json(name="sdk")
	val sdk: Int? = null,

	@Json(name="densities")
	val densities: List<Any?>? = null,

	@Json(name="dependencies")
	val dependencies: List<DependenciesItem?>? = null
)

data class ManualQa(

	@Json(name="date")
	val date: String? = null,

	@Json(name="tester")
	val tester: String? = null,

	@Json(name="status")
	val status: String? = null
)

data class Stats(

	@Json(name="prating")
	val prating: Prating? = null,

	@Json(name="pdownloads")
	val pdownloads: Long? = null,

	@Json(name="downloads")
	val downloads: Long? = null,

	@Json(name="rating")
	val rating: Rating? = null,

	@Json(name="subscribers")
	val subscribers: Long? = null,

	@Json(name="apps")
	val apps: Int? = null
)

data class Appcoins(

	@Json(name="flags")
	val flags: List<Any?>? = null,

	@Json(name="advertising")
	val advertising: Boolean? = null,

	@Json(name="billing")
	val billing: Boolean? = null
)

data class Time(

	@Json(name="seconds")
	val seconds: Double? = null,

	@Json(name="human")
	val human: String? = null
)

data class Info(

	@Json(name="time")
	val time: Time? = null,

	@Json(name="status")
	val status: String? = null
)
