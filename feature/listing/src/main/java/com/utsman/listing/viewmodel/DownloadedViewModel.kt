/*
 * Created by Muhammad Utsman on 13/12/20 2:17 AM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.listing.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.data.model.dto.downloaded.DownloadedApps
import com.utsman.listing.domain.DownloadedUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadedViewModel @ViewModelInject constructor(private val downloadedUseCase: DownloadedUseCase) :
    ViewModel() {

    val downloadedList = downloadedUseCase
        .list.asLiveData(viewModelScope.coroutineContext)

    fun markIsDone(downloadedApps: DownloadedApps) = CoroutineScope(Dispatchers.IO).launch {
        downloadedUseCase.markIsDone(this, downloadedApps)
    }
}