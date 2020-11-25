package com.utsman.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.nhaarman.mockitokotlin2.verify
import com.utsman.data.model.dto.AppsSealedView.AppsView
import com.utsman.data.repository.PagingAppRepository
import com.utsman.data.repository.PagingAppRepositoryImpl
import com.utsman.data.route.Services
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.viewmodel.PagingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PagingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val services = Mockito.mock(Services::class.java)

    private lateinit var pagingAppRepository: PagingAppRepository
    private lateinit var pagingUseCase: PagingUseCase
    private lateinit var pagingViewModel: PagingViewModel

    private val dataPaging = PagingData.from(emptyList<AppsView>())
    private val schedulerProvider = Dispatchers.Unconfined

    @Mock
    private lateinit var observerPaging: Observer<PagingData<AppsView>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(schedulerProvider)

        pagingAppRepository = PagingAppRepositoryImpl(services)
        pagingUseCase = PagingUseCase(pagingAppRepository)
        pagingViewModel = PagingViewModel(pagingUseCase)
    }

    @Test
    fun appsPagingSuccess() = runBlockingTest {
        pagingUseCase.pagingData.postValue(dataPaging)
        pagingViewModel.pagingData.observeForever(observerPaging)
        verify(observerPaging).onChanged(dataPaging)
    }

    @Test
    fun appsPagingFailed() = runBlockingTest {
        pagingUseCase.pagingData.postValue(null)
        pagingViewModel.pagingData.observeForever(observerPaging)
        verify(observerPaging).onChanged(null)
    }

}