/*
 * Created by Muhammad Utsman on 18/12/20 9:24 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.storeapps.domain.SettingsUseCase
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(private val useCase: SettingsUseCase) : ViewModel() {

    val isRoot get() = useCase.isRooted
}