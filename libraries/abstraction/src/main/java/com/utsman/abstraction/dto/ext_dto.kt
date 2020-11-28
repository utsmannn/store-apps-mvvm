/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.dto

import com.utsman.abstraction.listener.IResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

suspend fun <T: Any> fetch(call: suspend () -> T): Flow<ResultState<T>> = flow {
    emit(ResultState.Loading<T>())
    try {
        emit(ResultState.Success(data = call.invoke()))
    } catch (e: Throwable) {
        emit(ResultState.Error<T>(th = e))
    }
}

fun <T: Any>stateOf(): MutableStateFlow<ResultState<T>> = run {
    MutableStateFlow(ResultState.Idle())
}

inline fun <reified T: Any>ResultState<T>.listenOn(iResultState: IResultState<T>) = run {
    when (this) {
        is ResultState.Idle -> iResultState.onIdle()
        is ResultState.Loading -> iResultState.onLoading()
        is ResultState.Success -> iResultState.onSuccess(this.data)
        is ResultState.Error -> iResultState.onError(this.th)
    }
}