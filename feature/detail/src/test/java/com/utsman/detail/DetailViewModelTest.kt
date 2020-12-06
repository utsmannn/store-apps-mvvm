/*
 * Created by Muhammad Utsman on 4/12/20 2:17 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.utsman.abstraction.interactor.ResultState
import com.utsman.data.model.dto.detail.DetailView
import com.utsman.data.repository.meta.MetaRepository
import com.utsman.data.repository.meta.MetaRepositoryImpl
import com.utsman.data.route.Services
import com.utsman.detail.domain.DetailUseCase
import com.utsman.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailViewModelTest {

    private val PACKAGE_NAME = "com.utsman.gantenk"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val services = Mockito.mock(Services::class.java)

    private lateinit var metaRepository: MetaRepository
    private lateinit var detailUseCase: DetailUseCase
    private lateinit var detailViewModel: DetailViewModel

    private val dataView = DetailView(packageName = PACKAGE_NAME)
    private val returnDetailViewSuccessValue = ResultState.Success(dataView)
    private val returnDetailViewErrorValue: ResultState.Error<DetailView> = ResultState.Error(th = Throwable("Error"))

    private val schedulerProvider = Dispatchers.Unconfined

    @Mock
    private lateinit var observerDetailView: Observer<in ResultState<DetailView>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(schedulerProvider)

        metaRepository = MetaRepositoryImpl(services)
        detailUseCase = DetailUseCase(metaRepository)
        detailViewModel = DetailViewModel(detailUseCase)
    }

    @Test
    fun detailViewSuccess() = runBlockingTest {
        detailUseCase.detailView.value = returnDetailViewSuccessValue
        detailViewModel.detailView.observeForever(observerDetailView)
        verify(observerDetailView).onChanged(returnDetailViewSuccessValue)
        detailViewModel.getDetailView(PACKAGE_NAME)
    }

    @Test
    fun detailViewError() = runBlockingTest {
        detailUseCase.detailView.value = returnDetailViewErrorValue
        detailViewModel.detailView.observeForever(observerDetailView)
        verify(observerDetailView).onChanged(returnDetailViewErrorValue)
        detailViewModel.getDetailView("")
    }

}