/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository

import com.utsman.data.model.response.list.Aptoide
import com.utsman.data.repository.list.AppsRepository
import com.utsman.data.repository.list.AppsRepositoryImpl
import com.utsman.data.route.Services
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class AppsRepositoryTest {

    private val service = mock(Services::class.java)
    private lateinit var appsRepository: AppsRepository

    @Before
    fun setup() {
        appsRepository = AppsRepositoryImpl(service)
    }

    @Test
    fun appsSuccess() = runBlocking {
        `when`(service.topList()).thenReturn(Aptoide())

        val repo = appsRepository.getTopApps()
        assertEquals(repo, Aptoide())
    }

    @Test
    fun appsNullOrError() = runBlocking {
        `when`(service.topList()).thenReturn(null)

        val repo = appsRepository.getTopApps()
        assertEquals(repo, null)
    }
}