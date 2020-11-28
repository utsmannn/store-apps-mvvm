/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.const

import com.utsman.data.model.Category
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class CategoriesAppsTest {

    private val listActual = listOf("social", "shopping")
        .map { Category.buildFrom(it) }

    @Test
    fun testMockPaging() = runBlockingTest {
        val pageList = CategoriesApps.MockPaging.page(1)
        Assert.assertEquals(pageList, listActual)
    }
}