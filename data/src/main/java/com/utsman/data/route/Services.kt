package com.utsman.data.route

import com.utsman.data.model.Aptoide
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("/api/7/apps/get")
    suspend fun randomList(): Aptoide

    @GET("/api/7/apps/search")
    suspend fun searchList(
        @Query("query") query: String = "",
        @Query("page") page: Int = 1
    ): Aptoide
}