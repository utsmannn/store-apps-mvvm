/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.route

import com.utsman.abstraction.extensions.getValueSafeOf
import com.utsman.data.di._optionRepository
import com.utsman.data.model.response.detail.AptoideMeta
import com.utsman.data.model.response.list.Aptoide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {

    companion object {
        private val settingMaturity = getValueSafeOf(_optionRepository)
        var isMaturity = false

        init {
            CoroutineScope(Dispatchers.IO).launch {
                settingMaturity?.maturity()?.collect { settingData ->
                    isMaturity = settingData.value
                }
            }
        }
    }

    @GET("/api/7/apps/get/offset={offset}/sort=downloads/mature={maturity}")
    suspend fun topList(
        @Path("offset") offset: Int = 0,
        @Path("maturity") maturity: Boolean = isMaturity
    ): Aptoide

    @GET("/api/7/apps/get/group_name={query}/offset={offset}/sort=downloads/limit=10/mature={maturity}")
    suspend fun groupList(
        @Path("query") query: String = "",
        @Path("offset") offset: Int = 0,
        @Path("maturity") maturity: Boolean = isMaturity
    ): Aptoide

    @GET("/api/7/apps/search/query={query}/offset={offset}/limit={limit}/mature={maturity}")
    suspend fun searchList(
        @Path("query") query: String = "",
        @Path("offset") offset: Int = 0,
        @Path("limit") limit: Int = 25,
        @Path("maturity") maturity: Boolean = isMaturity
    ): Aptoide

    @GET("/api/7/app/getMeta/package_name={package_name}")
    suspend fun getMeta(
        @Path("package_name") packageName: String = ""
    ): AptoideMeta
}