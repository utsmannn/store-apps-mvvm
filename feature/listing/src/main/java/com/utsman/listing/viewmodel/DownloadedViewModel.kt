/*
 * Created by Muhammad Utsman on 13/12/20 2:17 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.listing.domain.DownloadedUseCase

class DownloadedViewModel @ViewModelInject constructor(private val downloadedUseCase: DownloadedUseCase) :
    ViewModel() {

    val downloadedList = downloadedUseCase
        .list.asLiveData(viewModelScope.coroutineContext)
}