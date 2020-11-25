package com.utsman.data.route

import com.utsman.data.model.Aptoide
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Services {

    @GET("/api/7/apps/get/offset={offset}/sort=downloads")
    suspend fun randomList(
        @Path("offset") offset: Int = 0
    ): Aptoide

    @GET("/api/7/apps/get/group_name={query}/offset={offset}/sort=downloads")
    suspend fun searchList(
        @Path("query") query: String = "",
        @Path("offset") offset: Int = 0
    ): Aptoide
}