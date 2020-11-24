package com.utsman.data

import com.utsman.data.model.Aptoide
import com.utsman.data.repository.AppsRepository
import com.utsman.data.repository.AppsRepositoryImpl
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
        `when`(service.randomList()).thenReturn(Aptoide())

        val repo = appsRepository.getRandomApps()
        assertEquals(repo, Aptoide())
    }

    @Test
    fun appsNullOrError() = runBlocking {
        `when`(service.randomList()).thenReturn(null)

        val repo = appsRepository.getRandomApps()
        assertEquals(repo, null)
    }
}