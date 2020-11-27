package com.utsman.data.repository

import com.utsman.data.model.response.list.Aptoide
import com.utsman.data.model.Category
import com.utsman.data.route.Services
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CategoriesAppsRepositoryTest {

    private val services = mock(Services::class.java)
    private lateinit var categoriesRepository: CategoriesRepository

    private val socialCategory = Category.simple {
        name = "Social"
        query = "social"
    }

    @Before
    fun setup() {
        categoriesRepository = CategoriesRepositoryImpl(services)
    }

    @Test
    fun categorySuccess() = runBlocking {
        `when`(services.groupList(socialCategory.query)).thenReturn(Aptoide())

        val repo = categoriesRepository.getCategory(socialCategory)
        assertEquals(repo, Aptoide())
    }
}