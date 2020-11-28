/*
 * Created by Muhammad Utsman on 28/11/20 4:06 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository

import com.utsman.data.model.response.detail.AptoideMeta
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.repository.meta.MetaRepositoryImpl
import com.utsman.data.route.Services
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class MetaRepositoryTest  {

    private val services = mock(Services::class.java)
    private lateinit var metaRepositoryTest: MetaRepository

    private val packageNameTest = "com.google.android.GoogleCamera"

    @Before
    fun setup() {
        metaRepositoryTest = MetaRepositoryImpl(services)
    }

    @Test
    fun metaSuccess() = runBlockingTest {
        `when`(services.getMeta(packageNameTest)).thenReturn(AptoideMeta())

        val repo = metaRepositoryTest.getDetail(packageNameTest)
        assertEquals(repo, AptoideMeta())
    }

    @Test
    fun metaNullOrError() = runBlockingTest {
        `when`(services.getMeta(packageNameTest)).thenReturn(null)

        val repo = metaRepositoryTest.getDetail(packageNameTest)
        assertEquals(repo, null)
    }
}