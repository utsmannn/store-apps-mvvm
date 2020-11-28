/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.route

import com.utsman.data.model.response.detail.AptoideMeta
import com.utsman.data.model.response.list.Aptoide
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {

    @GET("/api/7/apps/get/offset={offset}/sort=downloads")
    suspend fun topList(
        @Path("offset") offset: Int = 0
    ): Aptoide

    @GET("/api/7/apps/get/group_name={query}/offset={offset}/sort=downloads")
    suspend fun groupList(
        @Path("query") query: String = "",
        @Path("offset") offset: Int = 0
    ): Aptoide

    @GET("/api/7/apps/search/query={query}/offset={offset}")
    suspend fun searchList(
        @Path("query") query: String = "",
        @Path("offset") offset: Int = 0
    ): Aptoide

    @GET("/api/7/app/getMeta/package_name={package_name}")
    suspend fun getMeta(
        @Path("package_name") packageName: String = ""
    ): AptoideMeta
}