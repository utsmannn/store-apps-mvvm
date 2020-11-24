package com.utsman.data.route

import com.utsman.data.model.Aptoide
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Services {

    @GET("/api/7/apps/get/offset={offset}")
    suspend fun randomList(
        @Path("offset") offset: Int = 0
    ): Aptoide

    @GET("/api/7/apps/search/query={query}/offset={offset}")
    suspend fun searchList(
        @Path("query") query: String = "",
        @Path("offset") offset: Int = 0
    ): Aptoide
}