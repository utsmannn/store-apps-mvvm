package com.utsman.data.const

import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class CategoriesAppsTest {

    @Test
    fun testMockPaging() = runBlockingTest {
        val actual = listOf(CategoriesApps.tools, CategoriesApps.video)
        val pageList = CategoriesApps.MockPaging.page(0)
        Assert.assertEquals(pageList, actual)
    }
}